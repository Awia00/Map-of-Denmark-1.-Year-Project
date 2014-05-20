package DataStructure;

/**
 * There can only be one of this class.
 * Used to connect from classes to the database handler.
 * @author Aleksandar Jonovic
 */
public class Database 
{
    // The database interface
    private static DatabaseInterface db = null;
	private static DatabaseInterface osmParser = null;
    
    /**
     * This method gets the database interface for use.
     * @return DatabaseHandler as DatabaseInterface.
     */ 
    public static DatabaseInterface db(boolean isKrak)
    {
		if(isKrak)
		{
			if(db == null){db = new DatabaseHandler();}
			return db;
		}
		else
		{
			if(osmParser == null){osmParser = new OSMParser();}
			return osmParser;
		}
    }
}
