package com.citizant.fraudshield.exception;


import org.apache.commons.lang.StringEscapeUtils;

public class ExceptionBase extends Throwable {
	
    private static final long serialVersionUID = 1L;

    private String shortDesc = "";
    private String longDesc = "";


    /**
     * Constructor that sets the Process Type, Exception Type and Short Description
     * 
     * @param shortDesc		Short Description
     */
    protected ExceptionBase(String shortDesc) {
        super(shortDesc);
        this.shortDesc = shortDesc;
    }

    /**
     * Constructor that sets the Process Type, Exception Type, Short Description and Long Description
     * 
     * @param shortDesc		Short Description
     * @param longDesc		Long Description
     */
    protected ExceptionBase(String shortDesc, String longDesc) {
        super(shortDesc);   
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
    }

    /**
     * Constructor that sets the Process Type, Exception Type and Throwable
     * 
     * @param throwable		Throwable
     */
    protected ExceptionBase(Throwable throwable) {
        super(throwable);   
        this.shortDesc = StringEscapeUtils.escapeHtml(throwable.toString());
        this.longDesc = encode(throwable);
    }
    
    public static String encode(Throwable throwable) {
        String longDesc = StringEscapeUtils.escapeHtml(throwable.toString());
        StringBuilder sb = new StringBuilder(longDesc + "\r\n");
        
        for (StackTraceElement ste : throwable.getStackTrace()) {
            sb.append(StringEscapeUtils.escapeHtml(ste.toString()) + "\r\n");
        }

        return sb.toString();
    }  

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }
    

}
