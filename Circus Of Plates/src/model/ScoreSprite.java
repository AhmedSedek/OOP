package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ScoreSprite extends Sprite {

	
	private static final String FONT_NAME = "Arial";
	private static final int FONT_SIZE = 18;
	private static final int FIRST_X = 850, FIRST_Y = 100, SECOND_X = 10, SECOND_Y = 100;
	
	private int scoreValue;
	private int playerIndex;
	private int x, y;

	public ScoreSprite(int playerIndex, int scoreValue) {
		this.playerIndex = playerIndex;
		this.scoreValue = scoreValue;
	}

	@Override
	public void draw(GraphicsContext g) {
		setCoordinates();
		String text = "Score: " + Integer.toString(scoreValue);
		Font font = Font.font(FONT_NAME, FontWeight.BOLD, FONT_SIZE);
		g.setFont(font);
		g.setStroke(Color.BLACK);
		g.fillText(text, x, y);
		g.strokeText(text, x, y);
	}

	private void setCoordinates() {
		if (playerIndex == 1) {
			x = SECOND_X;
			y = SECOND_Y;
		} else if (playerIndex == 0) {
			x = FIRST_X;
			y = FIRST_Y;
		}
	}
}