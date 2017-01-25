package gui;

import java.io.File;

import controller.AudioController;
import controller.GameController;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.stage.FileChooser;

public class ButtonFactory {

	private static ButtonFactory buttonFactory;

	private static final String[] btnName = new String[] { "New", "Pause", "Resume", "Quit", null, "Save", "Load", null,
			"Mute", null, "Easy", "Medium", "Hard" };
	private static final int NEW = 0, PAUSE = 1, RESUME = 2, QUIT = 3, SAVE = 5, LOAD = 6, MUTE = 8, EASY = 10,
			MEDIUM = 11, HARD = 12;

	public static ButtonFactory getInstance() {
		if (buttonFactory == null) {
			buttonFactory = new ButtonFactory();
		}
		return buttonFactory;
	}

	public int getNodeCount() {
		return btnName.length;
	}

	private boolean isSeparator(int index) {
		return btnName[index] == null;
	}

	public Node createNode(int index, GameController gameController) {
		if (isSeparator(index)) {
			return new Separator();
		}
		return createButton(index, gameController);
	}

	private Node createButton(int index, GameController gameController) {
		Button curBtn = new Button(btnName[index]);
		switch (index) {
		case NEW:
			curBtn.setOnAction(e -> gameController.newGame());
			break;
		case PAUSE:
			curBtn.setOnAction(e -> gameController.pauseGame());
			break;
		case RESUME:
			curBtn.setOnAction(e -> gameController.resumeGame());
			break;
		case QUIT:
			curBtn.setOnAction(e -> Platform.exit());
			break;
		case SAVE:
			curBtn.setOnAction(e -> {
				gameController.pauseGame();
				FileChooser chooser = new FileChooser();
				chooser.setTitle("Load game");
				File saveFile = chooser.showSaveDialog(null);
				gameController.save(saveFile.toPath().toString());
			});
			break;
		case LOAD:
			curBtn.setOnAction(e -> {
				gameController.pauseGame();
				FileChooser chooser = new FileChooser();
				chooser.setTitle("Load game");
				File loadFile = chooser.showOpenDialog(null);
				gameController.load(loadFile.toPath().toString());
			});
			break;
		case MUTE:
			curBtn.setOnAction(e -> {
				if (curBtn.getText().equals("Mute")) {
					AudioController.getInstance().mute();
					curBtn.setText("Unmute");
				} else {
					AudioController.getInstance().unmute();
					curBtn.setText("Mute");
				}
			});
			break;
		case EASY:
			curBtn.setOnAction(e -> gameController.newGame(0));
			break;
		case MEDIUM:
			curBtn.setOnAction(e -> gameController.newGame(1));
			break;
		case HARD:
			curBtn.setOnAction(e -> gameController.newGame(2));
			break;
		default:
			throw new IllegalStateException();
		}
		return curBtn;
	}
}