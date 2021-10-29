## DEFINING SECTIONS
A section is used to split up properties and values.

Every property is put under a category, called a section. These sections must be used to get a property. Sections are surrounded by `[` and `]`.
An optional parent can be provided, using `:` after the name of the section, with the name of the parent afterwards, then closing the section.
All properties for a section must follow after.

__Example__:
```
[MySection : ParentSectionHere]
[MySection]
```

## DEFINING PROPERTIES IN SECTIONS
A property is a value to be put inside of a section.

Each property can store one (1) value. Although this value can store varying amounts of data, such as an array or text, it is only one object. Every property must end in a semicolon `;`. This means that the entire config can fit on one line, or as many lines as you want.

If no value is assigned to the property, then it will be null.

Different sections can have the same property (same name) as others,
and redefining properties is acceptable. If there are multiple properties with
the same name in the same section, the one defined last should be used.

__Example__:
```my_property = 42;
my_property="hehe";
```

## DEFINING CONSTANTS
Allow reference to other properties!

Constants are the exact same as properties, and are therefore usable as properties. The only change to constants is a single star `*` at the beginning of the property name.

This star means that you can refer to this property name as a value. Similar to
an access modifier.

Constants are NOT seperated in sections, although they may belong in one.
Constants can be referred even in another section.

__Example__:
```*my_constant = 42;
my_property = my_constant;
```

## VALUE TYPES
	What value can a property hold?
	
- Strings: Strings are surrounded by double quotes (") or single quotes (')
- Integers: Whole numbers, no decimal.
- Floating point numbers: Decimal numbers
- Boolean:true or false
- Arrays: Arrays are surrounded by braces ("{" and "}") and each value is seperated by a comma `,`. Arrays can have any type of value, even mixed in. Such as strings and integers!
- Constants: Property names within other properties.

## FULL FILE EXAMPLE
```js
# This is an example of a config for a game.
# These comments are also acceptable in files.

[Server]
ip = "10.0.0.1";
port = 32525;

[FallbackServer : Server]
ip = "10.0.0.2";
port = 13531;

[NetworkSettings]
dns_server="10.0.0.7";
use_encryption = true;
timeout = 18000;
ports = {
   18351,
   35132,
   54252,
   5132,
   7542
};

[GraphicsSettings]
max_framerate = 60;
vsync = true;
```
