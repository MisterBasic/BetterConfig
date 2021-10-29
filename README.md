# BetterConfig
BCFG (short for Better Configuration File-format... G) is a multi-purpose configuration format allows splitting up properties into sections. Each section will have it's own properties, and sections can inherit properties from other sections!

## Why use this format? Why not INI, JSON or TOML?

- JSON is probably the most obvious: It is NOT a configuration format.
- INI is a terrible format. No data types, array nesting, or anything other than text.
- TOML is suprising good, I didn't learn of it until after making this. The only problem is serialization, it wasn't designed to be serialized. BCFG can be serialized (it's just text, in fact the entire file can be one line long).
- YAML is a standardized mess. You can find issues with YAML using Google.

If you are coming from these formats, this format should be familar. The only difference is the symbols used.

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
	file = ConfigFile.readFile("configtest.bcfg"); // Handles IOException already.
	// If you want to handle the IOException:
	file = new ConfigFile(new File("configtest.bcfg"));
	try { file.read() } catch(IOException e) { /* handle exception here... */ }
	// Otherwise, ignore those two lines and just use ConfigFile.readFile()
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
