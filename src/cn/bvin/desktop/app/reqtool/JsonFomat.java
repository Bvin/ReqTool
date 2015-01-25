package cn.bvin.desktop.app.reqtool;

public class JsonFomat {

	private static String repeat(String content,int count) {
		for (int i = 0; i < count; i++) {
			content+=content;
		}
		return content;
	}
	
	public static String format(String json) {
		int i = 0;
		int length = 0;
		int indentLevel = 0;
		char currentChar;
		String tab = "    ";
		String newJson = "";
		boolean inString = false;
		for (i = 0,length = json.length(); i < length; i += 1) {
			currentChar = json.charAt(i);
			switch (currentChar) {
				case '{': 

				case '[': 
	                if (!inString) { 
	                    newJson += currentChar + "\n" + repeat(tab, indentLevel+1);
	                    indentLevel += 1; 
	                } else { 
	                    newJson += currentChar; 
	                }
	                break; 
	            case '}': 
	            case ']': 
	                if (!inString) { 
	                    indentLevel -= 1; 
	                    newJson += "\n" + repeat(tab, indentLevel) + currentChar; 
	                } else { 
	                    newJson += currentChar; 
	                } 
	                break; 
	            case ',': 
	                if (!inString) { 
	                    newJson += ",\n" + repeat(tab, indentLevel); 
	                } else { 
	                    newJson += currentChar; 
	                } 
	                break; 
	            case ':': 
	                if (!inString) { 
	                    newJson += ": "; 
	                } else { 
	                    newJson += currentChar; 
	                } 
	                break; 
	            case ' ':
	            case '\n':
	            case '\t':
	                if (inString) {
	                    newJson += currentChar;
	                }
	                break;
	            case '"': 
	                if (i > 0 && json.charAt(i - 1) != '\\') {
	                    inString = !inString; 
	                }
	                newJson += currentChar; 
	                break;
	            default: 
	                newJson += currentChar; 
	                break;    
			}
		}
		return newJson;
	}
}
