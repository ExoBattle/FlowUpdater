package fr.flowarg.flowupdater.versions;

import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.download.DownloadList;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.json.Mod;
import fr.flowarg.flowupdater.utils.IOUtils;
import fr.flowarg.flowupdater.integrations.IntegrationManager;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

public interface IModLoaderVersion
{
    /**
     * Attach {@link FlowUpdater} object to mod loaders, allow them to retrieve some information.
     * @param flowUpdater flow updater object.
     */
    void attachFlowUpdater(FlowUpdater flowUpdater);

    /**
     * Check if the current mod loader is already installed.
     * @param installDir the dir to check.
     * @return if the current mod loader is already installed.
     */
    boolean isModLoaderAlreadyInstalled(Path installDir);

    /**
     * Install the current mod loader in a specified directory.
     * @param dirToInstall folder where the mod loader is going to be installed.
     * @throws Exception if an I/O error occurred.
     */
    void install(Path dirToInstall) throws Exception;

    /**
     * Various setup before mod loader's installer launch.
     * @param dirToInstall folder where the mod loader is going to be installed.
     * @param stream Installer download stream.
     * @return a new {@link ModLoaderLauncherEnvironment} object.
     * @throws Exception is an I/O error occurred.
     */
    ModLoaderLauncherEnvironment prepareModLoaderLauncher(Path dirToInstall, InputStream stream) throws Exception;

    /**
     * Install all mods in the mods' directory.
     * @param modsDir mods directory.
     * @param integrationManager used to check loaded plugins.
     * @throws Exception if an I/O error occurred.
     */
    void installMods(Path modsDir, IntegrationManager integrationManager) throws Exception;

    /**
     * Get all processed mods / mods to process.
     * @return all processed mods / mods to process.
     */
    List<Mod> getMods();

    default void installAllMods(Path modsDir)
    {
        this.getDownloadList().getMods().forEach(mod -> {
            try
            {
                final Path modFilePath = modsDir.resolve(mod.getName());
                IOUtils.download(this.getLogger(), new URL(mod.getDownloadURL()), modFilePath);
                this.getCallback().onFileDownloaded(modFilePath);
            }
            catch (MalformedURLException e)
            {
                this.getLogger().printStackTrace(e);
            }
            this.getDownloadList().incrementDownloaded(mod.getSize());
            this.getCallback().update(this.getDownloadList().getDownloadedBytes(), this.getDownloadList().getTotalToDownloadBytes());
        });

        this.getDownloadList().getCurseMods().forEach(curseMod -> {
            try
            {
                final Path modFilePath = modsDir.resolve(curseMod.getName());
                IOUtils.download(this.getLogger(), new URL(curseMod.getDownloadURL()), modFilePath);
                this.getCallback().onFileDownloaded(modFilePath);
            }
            catch (MalformedURLException e)
            {
                this.getLogger().printStackTrace(e);
            }
            this.getDownloadList().incrementDownloaded(curseMod.getLength());
            this.getCallback().update(this.getDownloadList().getDownloadedBytes(), this.getDownloadList().getTotalToDownloadBytes());
        });
    }

    /**
     * Check if the minecraft installation already contains another mod loader installation not corresponding to this version.
     * @param dirToInstall Mod loader installation directory.
     * @return true if another version of mod loader is installed. false if not.
     * @throws Exception if an error occurred.
     */
    boolean checkModLoaderEnv(Path dirToInstall) throws Exception;

    /**
     * Get the {@link DownloadList} object.
     * @return download info.
     */
    DownloadList getDownloadList();

    /**
     * Get the {@link ILogger} object.
     * @return the logger.
     */
    ILogger getLogger();

    /**
     * Get the {@link IProgressCallback} object.
     * @return the progress callback.
     */
    IProgressCallback getCallback();

    class ModLoaderLauncherEnvironment
    {
        private final List<String> command;
        private final Path tempDir;

        public ModLoaderLauncherEnvironment(List<String> command, Path tempDir)
        {
            this.command = command;
            this.tempDir = tempDir;
        }

        public List<String> getCommand()
        {
            return this.command;
        }

        public Path getTempDir()
        {
            return this.tempDir;
        }
    }
}
