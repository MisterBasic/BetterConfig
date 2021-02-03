# BlixateConfig
This configuration type is a bit different to others, but for the most part it's still the same.

```js
[Section]
some-string = "This is a string";
the-meaning-of-life = 42;
float = 42.5;
file = "test.txt";

[AnotherSection]
hrm = {
  what = "WHAT!!",
  why = "WHY???",
  who = "WAIT WHO?!",
  yes = "ye",
  no = "nah"
}
```

Here is an example on how to get these from Java

```java
public static void main(String[] args) {
  ConfigFile file = new ConfigFile(new File("./test.bcfg"));
  try {
    file.read(); // read() loads the relevant data into memory.
  }catch(IOException e) {
    e.printStackTrace();
  }
  ConfigSection section = file.getSection("Section");
  ConfigProperty property = section.getProperty("some-string");
  System.out.println(property.getAsString());
}
```

or, if you are a mad lad:

```java
public static void main(String[] args) {
  ConfigFile file = new ConfigFile(new File("./test.bcfg"));
  try {
    file.read(); // read() loads the relevant data into memory.
  }catch(IOException e) {
    e.printStackTrace();
  }
  String string = file.getSection("Section").getProperty("some-string").getAsString();
  System.out.println(string);
}
```
