package application;

public class Theme {
	
	private String themeName, themePath;
	
	public Theme (String name) {
		themeName = name;
	}
	
	public Theme () {
		themePath = "application.css";
	}
	
	//set theme path
	public void setTheme(String name) {
			themeName = name;
			System.out.println("Theme = " + themeName);
			
			//Add paths here
			switch (name) {
			case "desktop": themePath = "themes/desktop.css";
			break;
			case "red": themePath = "themes/red.css";
			break;
			default: themePath = "application.css";
			}
			
			System.out.println("Theme set");
	}
	
	//get theme path
	public String getTheme () {
		return themePath;
	}
	
	

}
