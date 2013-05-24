package proteinprediction;

/**
 * 
 * @author Yann
 */
public interface ProgramEntryPoint {
    
    /**
     * The run method of this entry point.
     * 
     * Note, that function isn't static, because an interface doesn't support to
     * define static methods.
     * 
     * @return The exit code for the bash, see ProgramSettings PROGRAM_EXIT_ consts
     */
    public abstract int run(String args[]);
    
    /**
     * get a String with the usage/help of this entry point.
     */
    public abstract String getUsageAndHelp();
    
    /**
     * returns a short description of this entry point (shouldn't contain newlines)
     */
    public abstract String getShortDescription();
    
    /**
     * returns the commandline argument used, for entering this program mode.
     */
    public abstract String getCommandLineArgumentName();
    
}
