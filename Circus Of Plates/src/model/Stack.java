package model;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import shapes.CustomShape;
import shapes.RectangleShape;
import sprite.ShapeSprite;
import sprite.Sprite;

public class Stack {

	public static final int WIDTH = 50, HEIGHT = 5, RADIUS = 10, SHIFT = 15, BORDER_THICKNESS = 5;
	public static final int VERT_EPS = 10, HORT_EPS = 20;
	public static final int winCount = 3;

	private int score;
	private int xPosition, yPosition;
	private int playerIndex;
	private int heightSum;

	private ArrayList<Sprite> shapes;
	private StackState state;

	public Stack(int playerIndex) {
		this.playerIndex = playerIndex;
		yPosition = Avatar.AVATAR_HEIGHT;
		this.heightSum = yPosition;
		this.shapes = new ArrayList<Sprite>();
		this.state = new EmptyStack();
		score = 0;
	}

	public void setX(int x) {
		this.xPosition = x;
		updateList();
	}

	public Sprite getSprite() {
		return new ShapeSprite(
				new RectangleShape(xPosition, yPosition, WIDTH, HEIGHT, getStackFillColor(), getStackStrokeColor()));
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Sprite> getShapesSprite() {
		return (ArrayList<Sprite>) shapes.clone();
	}

	public void addShape(ShapeSprite shape) {
		heightSum -= shape.getHeight();
		shape.setY(heightSum);
		shape.setX(xPosition + BORDER_THICKNESS);
		shapes.add(shape);
		if (checkRemoval()) {
			removeShapes();
			addScore();
		}
		checkState();
	}

	public int getScore() {
		return score;
	}

	public void releaseShapes() {
		while (shapes.size() > 0) {
			ShapesPool.getInstance().releaseShape(((ShapeSprite) shapes.get(shapes.size() - 1)).getCustomShape());
			shapes.remove(shapes.size() - 1);
		}
	}

	public int getShapeCount() {
		return shapes.size();
	}

	public boolean attach(CustomShape shape) {
		if (canAttach(shape)) {
			shape.setCaught();
			addShape((ShapeSprite) shape.getSprite());
			return true;
		}
		return false;
	}

	private void checkState() {
		if (heightSum < 100) {
			state = new FullStack();
		}
	}

	private void removeShapes() {
		int removeCount = winCount;
		while (removeCount-- > 0) {
			heightSum += ((ShapeSprite) shapes.get(shapes.size() - 1)).getHeight();
			ShapesPool.getInstance().releaseShape(((ShapeSprite) shapes.get(shapes.size() - 1)).getCustomShape());
			shapes.remove(shapes.size() - 1);
		}
	}

	private void addScore() {
		score++;
	}

	private boolean checkRemoval() {
		int sameShape = 1;
		for (int i = shapes.size() - 2; i >= 0 && sameColor(shapes.get(i + 1), shapes.get(i)); i--) {
			sameShape++;
			if (sameShape == winCount) {
				return true;
			}
		}
		return false;
	}

	private boolean sameColor(Sprite sprite1, Sprite sprite2) {
		return ((ShapeSprite) sprite1).getColor() == ((ShapeSprite) sprite2).getColor();
	}

	private Color getStackFillColor() {
		if (playerIndex == 1) {
			return Color.RED;
		}
		return Color.CYAN;
	}

	private Color getStackStrokeColor() {
		if (playerIndex == 1) {
			return Color.BLACK;
		}
		return Color.GREEN;
	}

	private void updateList() {
		for (int i = 0; i < shapes.size(); i++) {
			((ShapeSprite) shapes.get(i)).setX(xPosition + BORDER_THICKNESS);
		}
	}

	private boolean canAttach(CustomShape shape) {
		return state.canAttach(shape, xPosition, heightSum);
	}

	public boolean checkStackFull() {
		return (state instanceof FullStack);
	}
}
