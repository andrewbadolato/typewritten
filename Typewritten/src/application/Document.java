package application;

public class Document {

	private String docText;
	private String title = "Typewritten";
	private String filename = "";
	
	public Document (String text) {
		docText = text;
	}
	
	//setText
	public void setDocText(String text) {
		this.docText = text;
	}
	
	//getText
	public String getDocText() {
		return docText;
	}
	
	//setFilename
	public void setFilename(String file)  {
		this.filename = file;
	}
	
	//getFilename
	public String getFilename() {
		return filename;
	}
	
	//getTitle
	public String getTitle() {
		return title;
	}
}
