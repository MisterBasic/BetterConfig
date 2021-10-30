# How to use BCFG files
## Sections
A section is defined in square brackets (`[` and `]`). Each section can have it's own properties. There is no section inheritance, unlike many other configuration formats. Properties defined in different sections can have the same name.
###
```
[Section]
some_property = 42

[AnotherSection]
some_property = 60
```
In this example, both properties exist, but depending on which section you access, they have different values.
## Properties
Every section has it's own properties. These can be defined with a name and value, but nothing else. To assign a value, you can either use an equals `=` or a colon `:`, which both mean the same thing. Whitespace is ignored, so spacing is up to you.
### Example
```
[Section]
property = 42
pi = 3.1415926
string = "hello there!"
language: "gibberish"
test : "this is also acceptable syntax, but your insane if you space this way."
```
## Arrays
When defining a property, you can define multiple elements inside one property. The types do not have to match, and mix-matching types is always acceptable. Nested arrays are also acceptable.
### Example
```
[idk]
arrays = { 1, 3.4, "Hello?", { 1, 2, 3, 4 } }
```
