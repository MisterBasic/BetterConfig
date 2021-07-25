# BetterConfig
BCFG (short for Better Configuration File-format... G) is a multi-purpose configuration format allows splitting up properties into sections. Each section will have it's own properties, and sections can inherit properties from other sections!

```py
[Settings]
language = "English";
sensitivity = 42.7;
unlocked_abilities = {
  "Fire",
  "Ice",
  "Double-jump"
};
[State]
random_stuff = {
  4, "A String", "Dynamic typing in arrays!?", 420.69
};
```
Here is an example on how to get these properties from Java:
```java
public static void main(String[] args) {
	ConfigFile file;
	file = new ConfigFile(new File("configtest.bcfg"));
	// OR
	file = ConfigFile.readFile("configtest.bcfg");
	try {
		file.read();
	} catch (IOException e) {
		System.out.println("Something went wrong! Couldn't load configuration file!");
		return;
	}
	ConfigSection settings = file.getSection("Settings");
	String language = settings.getProperty("language").getAsString();
	float mouse_sensitivity = settings.getProperty("sensitivity").getAsFloat();
	ConfigArray abilitiesArr = settings.getProperty("unlocked_abilities").asArray();

	System.out.println("Your language: " + language);
	System.out.println("Mouse Sensitivity: " + mouse_sensitivity);
	System.out.println("Abilties:");
	for(ConfigProperty ability : abilitiesArr.values()) {
		System.out.println(" - " + ability.getAsString());
	}
}
```
Here is the output:
```
Your language: English
Mouse Sensitivity: 42.7
Abilties:
 - Fire
 - Ice
 - Double-jump
```
