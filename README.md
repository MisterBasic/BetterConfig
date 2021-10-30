# BetterConfig
BCFG (short for Better Configuration File-format... G) is a multi-purpose configuration format allows splitting up properties into sections. Each section will have it's own properties, and sections can inherit properties from other sections!

## Why use this format? Why not INI, JSON or TOML?

- JSON is probably the most obvious: It is NOT a configuration format and there is no comments.
- INI is a mediocre format. There is no specification, and it's just text.
- TOML is too complicated, has too many features, and doesn't understand what it wants to be.
- YAML is a standardized mess. You can find issues with YAML using Google.
- **Note** StrictYAML is a better version of YAML, and if you need that type of format I do recommend it.

If you are coming from these formats, this format should be familar. The only difference is the symbols used.

```py
# Here is an example for settings.
[Settings]
language = "English"
sensitivity: 42.7 # You can use a '=' or a ':'!
unlocked_abilities = {
  "Fire",
  "Ice",
  "Double-jump"
}
[State]
random_stuff = {
  4, "A String", "Dynamic typing in arrays!?", 420.69, { 1, 2, 3, 4 }
}
```
Here is an example on how to get these properties from Java (The language that it's currently written in):
```java
public static void main(String[] args) {
	ConfigFile file = ConfigFile.readFile("configtest.bcfg"); // Handles IOException already.

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
