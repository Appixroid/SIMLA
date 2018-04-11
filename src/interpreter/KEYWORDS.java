package interpreter;

public class KEYWORDS 
{
	//Syntax constants
	public static final String INSTRUCTION_SEPARATOR = "\n";
	public static final String ARGUMENT_SEPARATOR = " ";
	public static final String FOR_SEPARATOR = ";";
	public static final String STRING_START = "\"";
	public static final String STRING_END = "\"";
	public static final String STRING_ESCAPE= "\\";
	public static final String EXPRESSION_START = "(";
	public static final String EXPRESSION_END = ")";
	public static final String ARRAY_INDEX_START = "[";
	public static final String ARRAY_INDEX_END = "]";
	public static final String ASSIGNMENT_KEYWORD = "in";
	public static final String ARRAY_SIZE_KEYWORD = "sized";
	public static final String TYPE_SET_KEYWORD = "typed";
	
	//Intructions constants
	public static final String COM_INSTRUCTION = "com";
	public static final String VAR_INSTRUCTION = "var";
	public static final String TAB_INSTRUCTION = "array";
	public static final String CALC_INSTRUCTION = "calc";
	public static final String CAST_INSTRUCTION = "cast";
	public static final String CALL_INSTRUCTION = "call";
	public static final String IF_INSTRUCTION = "if";
	public static final String IF_END = "endif";
	public static final String ELSE_INSTRUCTION = "else";
	public static final String ELSE_END = "endelse";
	public static final String ELSE_IF_INSTRUCTION = "elif";
	public static final String ELSE_IF_END = "endelif";
	public static final String LOOP_INSTRUCTION = "while";
	public static final String LOOP_END = "endwhile";
	public static final String FOR_INSTRUCTION = "for";
	public static final String FOR_END = "endfor";
	public static final String FUNC_INSTRUCTION = "func";
	public static final String FUNC_END = "endfunc";
	public static final String FUNC_RETURN = "return";
	
	//Functions constants
	public static final String TYPE_FUNCTION = "type";
	public static final String VERSION_FUNCTION = "version";
	public static final String OUTPUT_FUNCTION = "output";
	public static final String INPUT_FUNCTION = "input";
	public static final String RANDOM_FUNCTION = "random";
	public static final String SIZE_FUNCTION ="sizeof";
	
	//Types constants
	public static final String INTEGER_STRING = "Integer";
	public static final String REAL_STRING = "Real";
	public static final String STRING_STRING  = "String";
	public static final String BOOLEAN_STRING = "Boolean";
	public static final String NULL_STRING = "Null";
	public static final String VOID_STRING = "void";
	
	public static final String INTEGER_CLASS = "java.lang.Integer";
	public static final String REAL_CLASS = "java.lang.Double";
	public static final String STRING_CLASS = "java.lang.String";
	public static final String BOOLEAN_CLASS = "java.lang.Boolean";
	
	public static final String TRUE_VALUE = "true";
	public static final String FALSE_VALUE = "false";
}
