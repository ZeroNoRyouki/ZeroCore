package it.zerono.mods.zerocore.lib.client;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.versioning.ComparableVersion;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public final class VersionChecker {

    /**
     * Schedule a new version check for the calling mod.
     * Call this method during the pre-initialization or initialization phases
     *
     * @param url an HTTP/HTTPS URL for the web service responsible of providing the up-to-date version of the mod
     *
     *            The following parameters will automatically be added to the provided URL:
     *
     *            - mod : the mod-id of your mod
     *            - mc : the version of Minecraft currently running
     *
     *            The web service should reply with a single line of text. For successful requests, "OK" should be
     *            returned followed by the up-to-date version of the mod and an optional message. The semicolon (';')
     *            should be used to separate each component of the reply. Following are some example of successful replies:
     *
     *            OK;2.4.1;New version available with tons of bug fixes
     *            OK;1.0.1
     *
     *            The up-to-date version (and the current version of the mod) must be in a format compatible
     *            with {@link ComparableVersion}
     *
     *            If the request could no be completed successfully you should reply with an error message in the form
     *            of "ERR" followed by an the message itself:
     *
     *            ERR;Unknown mod-id or Minecraft version
     *            ERR;Unsupported Minecraft version
     *            ERR;Error XYZ while processing your request
     *
     *            The error message will be logged in the FML log
     *
     *            As a general advice, keep the update/error messages short
     */
    public static void scheduleCheck(String url) {

        final ModContainer mc = Loader.instance().activeModContainer();

        if (null == mc || null == url || url.isEmpty())
            return;

        final StringBuilder sb = new StringBuilder(url);

        sb.append("?mod=");
        sb.append(mc.getModId());
        sb.append("&mc=");
        sb.append(Loader.MC_VERSION);

        URL checkURL;

        try {

            checkURL = new URL(sb.toString());

        } catch (MalformedURLException ex) {

            ex.printStackTrace();
            return;
        }

        new VersionCheckerThread(new ModVersionData(mc, checkURL)).start();
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent evt) {

        List<ModVersionData> notifications = VersionChecker.getPendingNotifications();

        if (null == notifications)
            return;

        for (ModVersionData versionData: notifications) {

            if (null != versionData && versionData.isNewVersionAvailable()) {

                String updateMessage = versionData.getUpdateMessage();
                ITextComponent msg;

                if (null == updateMessage || updateMessage.isEmpty())
                    msg = new TextComponentTranslation("zerocore:vercheck.update1",
                            versionData.getLastVersion(), versionData.getName());
                else
                    msg = new TextComponentTranslation("zerocore:vercheck.update2",
                            versionData.getLastVersion(), versionData.getName(), updateMessage);

                evt.player.addChatMessage(msg);
            }
        }
    }

    private synchronized static void addNotification(ModVersionData data) {

        if (null == VersionChecker.s_pendingNotifications)
            VersionChecker.s_pendingNotifications = new ArrayList<>();

        VersionChecker.s_pendingNotifications.add(data);
    }

    private synchronized static List<ModVersionData> getPendingNotifications() {

        List<ModVersionData> notifications = VersionChecker.s_pendingNotifications;

        VersionChecker.s_pendingNotifications = null;
        return notifications;
    }

    private static class ModVersionData {

        public ModVersionData(ModContainer container, URL url) {

            this._container = container;
            this._checkURL = url;
            this._lastVersion = this._updateMessage = null;
        }

        public String getName() {
            return this._container.getName();
        }

        public URL getCheckURL() {
            return this._checkURL;
        }

        public String getCurrentVersion() {
            return this._container.getVersion();
        }

        public String getLastVersion() {
            return this._lastVersion;
        }

        public String getUpdateMessage() {
            return this._updateMessage;
        }

        public void update(String lastVersion, String updateMessage) {

            this._lastVersion = lastVersion;
            this._updateMessage = updateMessage;
        }

        public boolean isNewVersionAvailable() {

            ComparableVersion current = new ComparableVersion(this.getCurrentVersion());
            ComparableVersion last = new ComparableVersion(this.getLastVersion());

            return last.compareTo(current) > 0;
        }

        private final ModContainer _container;
        private final URL _checkURL;
        private String _lastVersion;
        private String _updateMessage;
    }

    private static class VersionCheckerThread extends Thread {

        public VersionCheckerThread(ModVersionData modVersionData) {

            this.setName("Zero CORE version checker thread");
            this._modData = modVersionData;
        }

        @Override
        public void run() {

            final HttpURLConnection cn = this.openConnection();
            String reply = null;

            try (
                    BufferedReader reader = new BufferedReader(new InputStreamReader(cn.getInputStream()))
            ) {
                reply = reader.readLine();

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (null == reply || reply.isEmpty())
                return;

            String[] tokens = reply.split(";");

            if (0 == tokens.length)
                return;

            if (0 == tokens[0].compareToIgnoreCase("OK") && tokens.length > 1) {

                String lastVersion = tokens[1];
                String updateMessage = tokens.length > 2 ? tokens[2] : "";

                this._modData.update(lastVersion, updateMessage);
                VersionChecker.addNotification(this._modData);

            } else {

                FMLLog.warning("Update check for mod %s failed : %s", this._modData.getName(), reply);
            }
        }

        private HttpURLConnection openConnection() {

            try {

                final HttpURLConnection cn = (HttpURLConnection) this._modData.getCheckURL().openConnection();

                cn.setConnectTimeout(3000);
                cn.setReadTimeout(3000);
                cn.setRequestMethod("GET");
                return cn;

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }

        private final ModVersionData _modData;
    }

    private static List<ModVersionData> s_pendingNotifications;
}
