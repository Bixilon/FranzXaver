# JavaFX SVG
===

(Fork of https://github.com/afester/FranzXaver)

Known issues
============

* Gradient transformations are not properly supported yet. There are some
  heuristic-based workarounds, but if skew or scale is part of an SVG gradient
  transformation the result shape is not accurately created. This is mainly
  due to JavaFX's lack of gradient transformation support.

* Sometimes SVG authoring tools save text as <flowRoot> element, not as <text>.
  Apache Batik does not seem to support <flowRoot> - a workaround is to convert
  the <flowRoot> to a <text> element.
  See also http://graphicdesign.stackexchange.com/questions/21662/how-does-inkscape-decide-whether-to-use-flowroot-or-text

## How to get it
### Maven
```xml
<dependency>
    <groupId>de.bixilon.javafx</groupId>
    <artifactId>javafx-svg</artifactId>
    <version>0.2</version>
</dependency>
```
### Gradle
```groovy
implementation 'de.bixilon.javafx:javafx-svg:0.2'
```
