package afester.javafx.examples.board.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class PartText implements PartShape {

    private final Point2D pos;
    private final Double size;          // text height in mm
    private final FontWeight weight;    // text weight
    private final StringBuffer text;

    @Deprecated
    public PartText(Double x, Double y, String text, Double size) {
        this.pos = new Point2D(x, y);
        this.text = new StringBuffer(text);
        this.size = size;
        this.weight = FontWeight.NORMAL;
    }

    public PartText(Point2D textPos, Double size, FontWeight weight) {
        this.pos = textPos;
        this.text = new StringBuffer();
        this.size = size;
        this.weight = weight;
    }

    public void append(String s) {
        text.append(s);
    }

    @Override
    public Shape createNode() {
        Text textShape = new Text(pos.getX(), pos.getY(), text.toString());

        Font theFont = Font.font("Courier", weight, size);	// TODO: The rendered text is messed up if the size is too small!
        textShape.setFont(theFont);
        textShape.setFill(Color.GRAY);
        textShape.setTextOrigin(VPos.BOTTOM); // .BASELINE);

        return textShape;
    }

    @Override
    public Node getXML(Document doc) {
        Element result = doc.createElement("text");
        result.setAttribute("x", Double.toString(pos.getX()));
        result.setAttribute("y", Double.toString(pos.getY()));
        result.setAttribute("size", size.toString());
        result.setAttribute("weight", weight.toString());
        result.setTextContent(text.toString());

        return result;
    }
    
    
    @Override
    public String toString() {
        return String.format("PartText[pos=%s, size=%s, text=\"%s\"]", pos, size, text);
    }

}
