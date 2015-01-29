package net.sf.JRecord.External.Def;

import java.util.HashMap;

import net.sf.JRecord.Common.Constants;
import net.sf.JRecord.Common.Conversion;
import net.sf.JRecord.Numeric.ConversionManager;
import net.sf.JRecord.Numeric.Convert;
import net.sf.JRecord.Types.Type;
import net.sf.JRecord.Types.TypeManager;

public class BasicConversion implements AbstractConversion {

	private static final int numberOfEntries;
	private static String[] names = new String [30] ;
    private static String[] externalNames = new String [30] ;
    private static int[] keys = new int[40];

    static {
       int i = 0; 
    	
 	   String rdDefault = "Default";
 	   String rdFixed = "Fixed Length Binary";
 	   String rdLineBin = "Line based Binary";
 	   String rdVb = "Mainframe VB (rdw based) Binary";
 	   String rdVbDump = "Mainframe VB Dump: includes Block length";
   	   String rdOcVb = "Open Cobol VB";

    	
        keys[i] = Constants.IO_DEFAULT;                externalNames[i] = "Default";                 names[i++] = rdDefault;
        keys[i] = Constants.IO_TEXT_LINE;              externalNames[i] = "Text";                    names[i++] = "Text IO";
        keys[i] = Constants.IO_BIN_TEXT;               externalNames[i] = "Byte_Text";               names[i++] = "Text IO (byte Based)";
        keys[i] = Constants.IO_UNICODE_TEXT;           externalNames[i] = "Text_Unicode";            names[i++] = "Text IO (Unicode)";
        keys[i] = Constants.IO_FIXED_LENGTH;           externalNames[i] = "Fixed_Length";            names[i++] = rdFixed;
        keys[i] = Constants.IO_FIXED_LENGTH_CHAR;      externalNames[i] = "Fixed_Length_Char";       names[i++] = "Fixed Length Char";
        keys[i] = Constants.IO_BINARY;                 externalNames[i] = "Binary";                  names[i++] = rdLineBin;
        keys[i] = Constants.IO_VB;                     externalNames[i] = "Mainframe_VB";            names[i++] = rdVb;
        keys[i] = Constants.IO_VB_DUMP;                externalNames[i] = "Mainframe_VB_As_RECFMU";  names[i++] = rdVbDump;
        keys[i] = Constants.IO_VB_FUJITSU;             externalNames[i] = "FUJITSU_VB";              names[i++] = "Fujitsu Variable Binary";
        keys[i] = Constants.IO_VB_OPEN_COBOL;          externalNames[i] = "Open_Cobol_VB";           names[i++] = rdOcVb;
        keys[i] = Constants.IO_MICROFOCUS;             externalNames[i] = "Microfocus_Format";       names[i++] = "Experimental Microfocus Header File";
        keys[i] = Constants.IO_UNKOWN_FORMAT;          externalNames[i] = "UNKOWN_FORMAT";           names[i++] = "Unknown File Format";
        keys[i] = Constants.IO_WIZARD;                 externalNames[i] = "FILE_WIZARD";             names[i++] = "File Wizard";
        keys[i] = Constants.IO_CSV;                    externalNames[i] = "CSV_EMBEDDED_CR";         names[i++] = "Csv Embedded Cr";
        keys[i] = Constants.IO_UNICODE_CSV;            externalNames[i] = "UNICODE_CSV_EMBEDDED_CR"; names[i++] = "Unicode Csv Embedded Cr";
        keys[i] = Constants.IO_NAME_1ST_LINE;          externalNames[i] = "CSV_NAME_1ST_LINE";       names[i++] = "Csv Name on 1st line";
        keys[i] = Constants.IO_CSV_NAME_1ST_LINE;      externalNames[i] = "CSV_NAME_1ST_LINE_EMBEDDED_CR"; names[i++] = "Csv Name on 1st line (Embedded Cr)";
        keys[i] = Constants.IO_UNICODE_NAME_1ST_LINE;  externalNames[i] = "UNICODE_CSV_NAME_1ST_LINE_";    names[i++] = "Unicode Name on 1st line";
        keys[i] = Constants.IO_UNICODE_CSV_NAME_1ST_LINE;externalNames[i] = "UNICODE_CSV_NAME_1ST_LINE_EMBEDDED_CR";      names[i++] = "Unicode Name on 1st line (Embedded Cr)";

        keys[i] = Constants.IO_GENERIC_CSV;            externalNames[i] = "CSV_GENERIC";            names[i++] = "Generic CSV (Choose details at run time)";

        keys[i] = Constants.IO_XML_USE_LAYOUT;         externalNames[i] = "XML_Use_Layout";         names[i++] = "XML - Existing Layout";
        keys[i] = Constants.IO_XML_BUILD_LAYOUT;       externalNames[i] = "XML_Build_Layout";       names[i++] = "XML - Build Layout";
        keys[i] = Constants.NULL_INTEGER;              externalNames[i] = null;                     names[i] = null;
        
        numberOfEntries = i;
    }
    
	private String[] typeNames ;
	private HashMap<String, Integer> typeNumbers;
	
	private HashMap<String, Integer> dialectLookup = new HashMap<String, Integer>(50);
	private HashMap<Integer, String> dialectNameLookup = new HashMap<Integer, String>(50);

	/**
	 * Basic Type / Format conversion (for use in JRecord; RecordEditor has
	 * its own (database based conversion).
	 */
	@SuppressWarnings("deprecation")
	public BasicConversion() {
		TypeManager manager = TypeManager.getInstance();
		typeNames = new String[manager.getNumberOfTypes()];
		typeNumbers = new HashMap<String, Integer>(manager.getNumberOfTypes() * 2);

		for (int i=0; i < typeNames.length; i++) {
			typeNames[i] = "";
		}

		setName(Type.ftChar  , "Char");
		setName(Type.ftNumAnyDecimal  , "Number any decimal", "NumAnyDecimal");
		setName(Type.ftPositiveNumAnyDecimal  , "PositiveNumAnyDecimal", "Number (+ve) any decimal");
		setName(Type.ftCharRightJust      , "Char (right justified)");
		setName(Type.ftCharNullTerminated , "Char Null terminated");
		setName(Type.ftCharNullPadded     , "Char Null padded");
		setName(Type.ftHex                , "Hex Field");
		setName(Type.ftNumLeftJustified   , "Num (Left Justified)");
		setName(Type.ftNumRightJustified  , "Num (Right Justified space padded)");
		setName(Type.ftNumRightJustifiedPN, "Num (Right Justified space padded) +/- sign");
		setName(Type.ftNumRightJustCommaDp, "Num (Right Just space padded, \",\" Decimal)");
		setName(Type.ftNumRightJustCommaDpPN, "Num (Right Just space padded, \",\" Decimal) +/- sign", "Num (Right Just space padded, \",\" Decimal) +/- sig");
		setName(Type.ftNumZeroPadded      , "Num (Right Justified zero padded)", "Zero Padded Number with sign=+/-");
		setName(Type.ftNumZeroPaddedPN    , "Num (Right Justified zero padded +/- sign)", "Zero Padded Number with sign=+/-");
		setName(Type.ftAssumedDecimal         , "Num Assumed Decimal (Zero padded)");
		setName(Type.ftAssumedDecimalPositive , "Num Assumed Decimal (+ve)");
		setName(Type.ftNumZeroPaddedPositive  , "Num (Right Justified zero padded positive)", "Positive Zero Padded Number");
		setName(Type.ftNumCommaDecimal        , "Zero Padded Number decimal=\",\"");
		setName(Type.ftNumCommaDecimalPN      , "Zero Padded Number decimal=\",\" sign=+/-");
		setName(Type.ftNumCommaDecimalPositive, "Zero Padded Number decimal=\",\" (only +ve)");


		setName(Type.ftSignSeparateLead   , "Num Sign Separate Leading");
		setName(Type.ftSignSeparateTrail  , "Num Sign Separate Trailing");
		setName(Type.ftDecimal            , "Decimal");
		setName(Type.ftBinaryInt          , "Binary Integer");
		setName(Type.ftBinaryIntPositive  , "Binary Integer (only +ve)");
		setName(Type.ftPostiveBinaryInt   , "Postive Binary Integer");
		setName(Type.ftFloat              , "Float");
		setName(Type.ftDouble             , "Double");
		setName(Type.ftBit  , "Bit");
		setName(Type.ftPackedDecimal         , "Mainframe Packed Decimal (comp-3)");
		setName(Type.ftPackedDecimalPostive  , "Mainframe Packed Decimal (+ve)");
		setName(Type.ftZonedNumeric  , "Mainframe Zoned Numeric");
		typeNumbers.put("Binary Integer Big Edian (Mainframe, AIX etc)".toLowerCase(), Integer.valueOf(Type.ftBinaryBigEndian ));
		setName(Type.ftBinaryBigEndian  , "Binary Integer Big Endian (Mainframe?)", "Binary Integer Big Endian (Mainframe, AIX etc)");
		setName(Type.ftBinaryBigEndianPositive  , "Binary Integer Big Endian (only +ve)", "Binary Integer Big Endian (only +ve )");
		setName(Type.ftPositiveBinaryBigEndian  , "Positive Integer Big Endian", "Positive Integer (Big Endian)");
		setName(Type.ftFjZonedNumeric  , "Fujitsu Zoned Numeric");


		setName(Type.ftRmComp, "Rm Cobol Comp");
		setName(Type.ftRmCompPositive  , "Rm Cobol Comp (+ve)", "RM Cobol Positive Comp");
		setName(Type.ftCheckBoxBoolean , "Check Box (Boolean)");


		setName(Type.ftDate  , "Date - Format in Parameter field");
		setName(Type.ftDateYMD  , "Date - YYMMDD");
		setName(Type.ftDateYYMD  , "Date - YYYYMMDD");
		setName(Type.ftDateDMY  , "Date - DDMMYY");
		setName(Type.ftDateDMYY  , "Date - DDMMYYYY");
		setName(Type.ftCheckBoxTrue  , "Check Box True / Space");
		setName(Type.ftCheckBoxY  , "Checkbox Y/<null>", "CheckBox Y/null");
		setName(Type.ftCheckBoxYN  , "Checkbox Y/N");
		setName(Type.ftCheckBoxTF  , "Checkbox T/F");
		setName(Type.ftCsvArray  , "CSV array");
		setName(Type.ftXmlNameTag  , "XML Name Tag");
		setName(Type.ftMultiLineEdit  , "Edit Multi Line field");

		setName(Type.ftCharRestOfFixedRecord  , "Char Rest of Fixed Length");
		setName(Type.ftCharRestOfRecord  , "Char Rest of Record");
		setName(Type.ftMultiLineChar  , "Char (Multi-Line)", "Char Multi Line");
		
		
		setName(Type.ftCharMultiLine  , "Char (Multi-Line)");
		setName(Type.ftHtmlField  , "Html Field");
		setName(Type.ftArrayField, "Array Field");
		
		ConversionManager dialectMgr = ConversionManager.getInstance();
		
		for (int i = 0; i < dialectMgr.getNumberOfEntries(); i++) {
			Convert converter = dialectMgr.getConverter(i);
			String name = Conversion.replace(converter.getName(), " ", "_").toString();
			dialectLookup.put(name.toLowerCase(), converter.getIdentifier());
			dialectLookup.put(converter.getName().toLowerCase(), converter.getIdentifier());
			dialectLookup.put(Integer.toString(converter.getIdentifier()), converter.getIdentifier());
			dialectNameLookup.put(converter.getIdentifier(), name);
		}
	}

	/**
	 * Set The type name
	 * @param type type Id
	 * @param name Type Name
	 */
	private void setName(int type, String name) {
		typeNames[TypeManager.getInstance().getIndex(type)] = name;
		typeNumbers.put(name.toLowerCase(), Integer.valueOf(type));
		if (name.length() > 40) {
			typeNumbers.put(name.toLowerCase().substring(0, 40), Integer.valueOf(type));
		}
	}

	/**
	 * Set The type name
	 * @param type type Id
	 * @param name Type Name
	 */
	private void setName(int type, String name, String altname) {
		setName(type, name);
		typeNumbers.put(altname.toLowerCase(), Integer.valueOf(type));
		if (altname.length() > 40) {
			typeNumbers.put(altname.toLowerCase().substring(0, 40), Integer.valueOf(type));
		}
	}


	@Override
	public int getFormat(int idx, String format) {
		if (format != null && ! "".equals(format)) {
			try {
				return Integer.parseInt(format);
			} catch (Exception e) {
			}
		}
		return 0;
	}

	@Override
	public String getFormatAsString(int idx, int format) {
		// TODO Auto-generated method stub
		return Integer.toString(format);
	}

	@Override
	public int getType(int idx, String type) {
		String key = type.toLowerCase();
		if (typeNumbers.containsKey(key)) {
			return (typeNumbers.get(key)).intValue();
		}
		return 0;
	}
	

	@Override
	public String getTypeAsString(int idx, int type) {
		String s = typeNames[TypeManager.getInstance().getIndex(type)];
		if (s == null || "".equals(s)) {
			s = Integer.toString(type);
		}
		return s;
	}

	
	public boolean isValidTypeName(String name) {

		return name != null && name.length() > 0 && typeNumbers.containsKey(name.toLowerCase());
	}

	/* (non-Javadoc)
	 * @see net.sf.JRecord.External.Def.AbstractConversion#isValid(int, int)
	 */
	@Override
	public boolean isValid(int idx, int type) {
		String s = typeNames[TypeManager.getInstance().getIndex(type)];
		return s != null && ! "".equals(s);
	}

	
	@Override
	public int getDialect(String name) {
		int ret = -1;
		
		if (name != null) {
			Integer lookup = dialectLookup.get(name.trim().toLowerCase());
			if (lookup != null) {
				ret = lookup;
			} else {
				try {
					ret = Integer.parseInt(name);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}
		
		return ret;
	}
	
	
	@Override
	public String getDialectName(int key) {
		String ret = "";
				
		String lookup = dialectNameLookup.get(key);
		if (lookup != null) {
			ret = lookup;
		}
		
		return ret;
	}

    /* (non-Javadoc)
	 * @see net.sf.JRecord.IO.AbstractLineIOProvider#getStructureName(int)
	 */
    public static String getStructureName(int fileStructure) {
    	for (int i = 0; i < keys.length && keys[i] != Constants.NULL_INTEGER; i++) {
    		if (keys[i] == fileStructure) {
    			return externalNames[i];
    		}
    	}
    	return "";
    }


	public static String getStructureNameForIndex(int index) {
		return externalNames[index];
	}


	/**
     * Convert a structure-name to a file-Structure identifier
     * @param name Name of the File Structure
     * @return The file Structure
     */
    public static int getStructure(String name) {
    	for (int i = 0; i < keys.length && keys[i] != Constants.NULL_INTEGER; i++) {
    		if (externalNames[i].equalsIgnoreCase(name)) {
    			return keys[i];
    		}
    	}
    	return Constants.NULL_INTEGER;
    }



    public static int getFileStructureForIndex(int idx) {
    	return keys[idx];
    }




    public static String getFileStructureNameForIndex(int idx) {
    	return names[idx];
    }



    public static int getNumberOfFileStructures() {
    	return numberOfEntries;
    }

}
