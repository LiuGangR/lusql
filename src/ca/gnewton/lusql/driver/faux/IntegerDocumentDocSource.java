package ca.gnewton.lusql.driver.faux;

import ca.gnewton.lusql.metadata.*;
import java.lang.annotation.*;
import ca.gnewton.lusql.core.*;
import ca.gnewton.lusql.util.*;
import java.util.*;

/**
 * Describe class IntegerDocumentDocSource here.
 *
 *
 * Created: Wed Apr 22 15:38:56 2009
 *
 * @author <a href="mailto:gnewton@">Glen Newton</a>
 * @version 1.0
 */
public class IntegerDocumentDocSource 
    extends AbstractDocSource 
{
    public String description()
	{
	    return "Test source that creates 100 documents with fields testField1 and testField2 with integer values 0-100, and 7*(0-100) respectively";
	}
    /**
     * Creates a new <code>IntegerDocumentDocSource</code> instance.
     *
     */
    public IntegerDocumentDocSource() {

    }

// Implementation of ca.gnewton.lusql.core.Plugin

    /**
     * Describe <code>init</code> method here.
     *
     * @param properties a <code>Properties</code> value
     * @exception PluginException if an error occurs
     */
    @Override
     
    public final void init(final MultiValueProp properties) 
	throws PluginException 
	{

	}

    /**
     * Describe <code>explainProperties</code> method here.
     *
     * @return a <code>Map</code> value
     */
    public final Properties explainProperties() {
	return null;
    }

    /**
     * Describe <code>done</code> method here.
     *
     * @exception PluginException if an error occurs
     */
    public final void done() throws PluginException {

    }

    int i = 0;
// Implementation of ca.gnewton.lusql.core.DocSource

    Random r = new Random();
    /**
     * Describe <code>next</code> method here.
     *
     * @return a <code>Doc</code> value
     * @exception DataSourceException if an error occurs
     */

    public final static String PrimaryKeyField = "id";
    public final static String SimpleIntField = "intField";
    public final static String FakeTextField = "fakeTextField";
    
    int count = 0;
    
    public final Doc next() throws DataSourceException 
    {
	if(count > numDocs)
	    return new DocImp().setLast(true);	
	++count;
	
	Doc doc = new DocImp();
	doc.addField(PrimaryKeyField, Integer.toString(i));
	doc.addField(SimpleIntField, Integer.toString(i*7));
	StringBuilder sb = new StringBuilder();
	
	for(int j=0; j<r.nextInt(numDocs); j++)
	    sb.append("word" + r.nextInt(100000) + " ");
	doc.addField(FakeTextField, sb.toString());
	
	i++;
	return doc;
    }

    /**
     * Describe <code>addField</code> method here.
     *
     * @param string a <code>String</code> value
     */
    public final void addField(final String string) {

    }

    
    int numDocs = 5000;
    @PluginParameter(description="Set num docs", optional=true)
    public void setNumDocs(int n)
    {
	System.out.println("setNumDocs: setting from " + numDocs
			   + " to " + n);
	
	numDocs = n;
    }
    
    @PluginParameter(description="isThreadSafe", optional=true, isList=true)
    public void addFoo(int s)
    {
	
    }
    

    public boolean isThreadSafe()
	{
	    return false;
	}

    public String showState(int n)
    {
	StringBuilder sb = new StringBuilder();
	sb.append(super.showState(n));
	return sb.toString();
    }
}