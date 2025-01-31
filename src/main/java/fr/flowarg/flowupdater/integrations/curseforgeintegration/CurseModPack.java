package fr.flowarg.flowupdater.integrations.curseforgeintegration;

import java.util.List;

/**
 * Basic object that represents a CurseForge's mod pack.
 */
public class CurseModPack
{
    private final String name;
    private final String version;
    private final String author;
    private final List<CurseModPackMod> mods;

    CurseModPack(String name, String version, String author, List<CurseModPackMod> mods)
    {
        this.name = name;
        this.version = version;
        this.author = author;
        this.mods = mods;
    }

    /**
     * Get the mod pack's name.
     * @return the mod pack's name.
     */
    public String getName()
    {
        return this.name;
    }


    /**
     * Get the mod pack's version.
     * @return the mod pack's version.
     */
    public String getVersion()
    {
        return this.version;
    }

    /**
     * Get the mod pack's author.
     * @return the mod pack's author.
     */
    public String getAuthor()
    {
        return this.author;
    }

    /**
     * Get the mods in the mod pack.
     * @return the mods in the mod pack.
     */
    public List<CurseModPackMod> getMods()
    {
        return this.mods;
    }

    /**
     * A Curse Forge's mod from a mod pack.
     */
    public static class CurseModPackMod extends CurseMod
    {
        private final boolean required;

        CurseModPackMod(String name, String downloadURL, String md5, int length, boolean required)
        {
            super(name, downloadURL, md5, length);
            this.required = required;
        }

        CurseModPackMod(CurseMod base, boolean required)
        {
            this(base.getName(), base.getDownloadURL(), base.getMd5(), base.getLength(), required);
        }

        /**
         * Is the mod required.
         * @return if the mod is required.
         */
        public boolean isRequired()
        {
            return this.required;
        }
    }
}
