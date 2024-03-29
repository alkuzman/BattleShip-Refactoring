package Battleships;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import Battleships.Graphics.*;

public class GUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	BattleShipsEngine data = new BattleShipsEngine();

	public GUI(GameState paramGameState) {
		super("Battleships");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout(2, 1));
		this.setResizable(false);
		setHoriz(true);

		data.setState(paramGameState);

		// attack panel
		Container APanel = new Container();
		APanel.setLayout(new GridLayout());

		// home panel
		Container HPanel = new Container();
		HPanel.setLayout(new GridLayout());

		// influence map
		Container IMPanel = new Container();
		IMPanel.setLayout(new GridLayout());

		// center panel where grids are
		Container CenterPanel = new Container();
		CenterPanel.setLayout(new GridLayout(1, 3));
		// create grids and maps

		data.setDataPanels(this);

		APanel.add(data.getAttackPanel());
		CenterPanel.add(APanel);

		HPanel.add(data.getHomePanel());
		CenterPanel.add(HPanel);

		IMPanel.add(data.getInfluenceMapPanel());
		CenterPanel.add(IMPanel);

		/*
		 * Creates the southPanel. This panel holds the ship status and rotate
		 * button
		 */
		Container southPanel = new Container();
		southPanel.setLayout(new GridLayout(1, 2));
		southPanel.setSize(400, 400);
		/*
		 * Creates the shipPanel which contains the status of ships
		 */

		Container shipPanel = new Container();
		shipPanel.setLayout(new GridLayout(4, 2));
		// adds the Ship Panel to the south panel
		southPanel.add(shipPanel);

		/*
		 * Creates the topShipPanel. This is where the placeholders for the top
		 * row of ships are
		 */

		Container topShipPanel = new Container();
		topShipPanel.setLayout(new FlowLayout());
		// add topShipPanel to shipPanel
		shipPanel.add(topShipPanel);

		/*
		 * Creates the topShipLabelPanel. This is where the labels for the top
		 * row of ships are
		 */

		Container topShipLabelPanel = new Container();
		topShipLabelPanel.setLayout(new FlowLayout());
		// add topShipPanel to shipPanel
		shipPanel.add(topShipLabelPanel);

		/*
		 * Creates the bottomShipPanel. This is where the placeholders for the
		 * top row of ships are
		 */

		Container bottomShipPanel = new Container();
		bottomShipPanel.setLayout(new FlowLayout());
		// add bottomShipPanel to shipPanel
		shipPanel.add(bottomShipPanel);

		/*
		 * Creates the bottomShipLabelPanel. This is where the labels for the
		 * bottom row of ships are
		 */

		Container bottomShipLabelPanel = new Container();
		bottomShipLabelPanel.setLayout(new FlowLayout());
		// add bottomShipPanel to shipPanel
		shipPanel.add(bottomShipLabelPanel);

		JButton NewButton = new JButton("New Game");
		topShipPanel.add(NewButton);
		// NewButton.addMouseListener(new NewButtonAction(this));

		JButton hideButton = new JButton("Hide Influence Map");
		topShipPanel.add(hideButton);
		hideButton.addMouseListener(new HideButtonAction(this));

		JButton destButton = new JButton("Rotate");
		topShipPanel.add(destButton);

		JButton rotateButton = new JButton("Rotate Ship");
		rotateButton.addMouseListener(new RotateButtonAction(this));
		bottomShipPanel.add(rotateButton);

		JButton quitButton = new JButton("Quit");
		quitButton.addMouseListener(new QuitButtonAction());
		bottomShipPanel.add(quitButton);

		/*
		 * Creates the container to go in the right hand side of the southPanel.
		 * This is where the rotate button will be.
		 */

		Container rotatePanel = new Container();
		rotatePanel.setLayout(new BorderLayout());
		// add rotatePanel to southPanel
		southPanel.add(rotatePanel);

		JButton viewMap = new JButton("View Influence Map");
		viewMap.addMouseListener(new ShowButtonAction(this));
		rotatePanel.add(viewMap, BorderLayout.NORTH);

		setOutText(new JTextField("lookat me!"));
		getOutText().setText(
				"Welcome To Battleships. Place ships on the middle grid");
		getOutText().setEditable(false);
		rotatePanel.add(getOutText());

		contentPane.add(CenterPanel, BorderLayout.CENTER);
		contentPane.add(southPanel, BorderLayout.SOUTH);

		this.pack();
		this.setSize(640, 400);
		this.setVisible(true);

	}

	public void setOut(String s) {
		getOutText().setText(s);
	}

	public void repaint() {
		data.repaint();
	}

	public boolean getPaintMineSunk() {
		return data.isPaintMineSunk();
	}

	public void setPaintMineSunk() {
		data.setPaintMineSunk(true);
	}

	public boolean getPaintDestSunk() {
		return data.isPaintDestSunk();
	}

	public void setPaintDestSunk() {
		data.setPaintDestSunk(true);
	}

	public boolean getPaintSubSunk() {
		return data.isPaintSubSunk();
	}

	public void setPaintSubSunk() {
		data.setPaintSubSunk(true);
	}

	public boolean getPaintBattleSunk() {
		return data.isPaintBattleSunk();
	}

	public void setPaintBattleSunk() {
		data.setPaintBattleSunk(true);
	}

	public boolean getPaintAirSunk() {
		return data.isPaintAirSunk();
	}

	public void setPaintAirSunk() {
		data.setPaintAirSunk(true);
	}

	public void reset() {
		data.resetEngine();

		setHoriz(true);
		
		this.pack();
		this.setSize(640, 400);
		this.setVisible(true);

	}

	public boolean checkAirPlaced() {
		return this.data.getGameState().getPlayerHomeGrid().checkAirPlaced();
	}

	public String placeAir(int i, int j) {
		String out = "";
		if (!checkAirPlaced()) {
			boolean valid;
			if (isShipRotatedHorizonally()) {
				valid = data.getGameState().getPlayerHomeGrid().addAir(i, j, 0);
			} else {
				valid = data.getGameState().getPlayerHomeGrid().addAir(i, j, 1);
			}

			if (valid) {
				Graphics hp = data.getHomePanel().getGraphics();
				if (isShipRotatedHorizonally()) {
					AircraftCarrierH.paint(hp, (j * 20), (i * 20));
				} else {
					AircraftCarrier.paint(hp, (j * 20), (i * 20));
				}

				out = out + data.getGameState().getPlayerHomeGrid().toString();
				data.getGameState().getPlayerHomeGrid().setAirPlaced();
				getOutText().setText("Air Placed");
			} else {
				if (isShipRotatedHorizonally()) {
					getOutText().setText("Aircraft Carrier Will Not Fit Here");
				}
				out = "not valid"
						+ data.getGameState().getPlayerHomeGrid().toString();// isto
			}
		}
		return out;
	}

	private boolean battleshipCanBePlaced() {
		return (checkAirPlaced() && !data.isBattlePlaced());
	}

	public String placeBattle(int i, int j) {
		String out = "";
		if (battleshipCanBePlaced()) {
			boolean valid;
			if (isShipRotatedHorizonally()) {
				valid = data.getGameState().getPlayerHomeGrid().addBattle(i, j, 0);
			} else {
				valid = data.getGameState().getPlayerHomeGrid().addBattle(i, j, 1);
			}

			if (valid) {
				Graphics hp = data.getHomePanel().getGraphics();
				if (isShipRotatedHorizonally()) {
					BattleshipH.paint(hp, (j * 20), (i * 20));
				} else {
					Battleship.paint(hp, (j * 20), (i * 20));
				}
				out = out + data.getGameState().getPlayerHomeGrid().toString();
				data.setBattlePlaced(true);
				if (!isShipRotatedHorizonally()) {
					getOutText().setText("Battleship Placed");
				}
			} else {
				out = "not valid"
						+ data.getGameState().getPlayerHomeGrid().toString();
				getOutText().setText("Battleships Will Not Fit Here");
			}

		}

		return out;
	}

	private boolean destroyerCanBePlaced() {
		return (checkAirPlaced() && data.isBattlePlaced() && !data.isDestPlaced());
	}

	public String placeDest(int i, int j) {
		String out = "";
		if (destroyerCanBePlaced()) {
			boolean valid;
			if (isShipRotatedHorizonally()) {

				valid = data.getGameState().getPlayerHomeGrid().addDest(i, j, 0);
			} else {
				valid = data.getGameState().getPlayerHomeGrid().addDest(i, j, 1);
			}
			Graphics hp = data.getHomePanel().getGraphics();

			if (valid) {
				if (isShipRotatedHorizonally()) {
					DestroyerH.paint(hp, (j * 20), (i * 20));
				} else {
					Destroyer.paint(hp, (j * 20), (i * 20));
				}
				out = out + data.getGameState().getPlayerHomeGrid().toString();
				data.setDestPlaced(true);
				getOutText().setText("Destroyer Placed");
			} else {
				out = "not valid"
						+ data.getGameState().getPlayerHomeGrid().toString();
				if (isShipRotatedHorizonally()) {
					getOutText().setText("Destroyer Will Not Fit Here");
				}
			}
		}

		return out;
	}

	public boolean submarineCanBePlaced() {
		return (checkAirPlaced()
				&& data.getGameState().getPlayerHomeGrid().checkBattlePlaced()
				&& data.getGameState().getPlayerHomeGrid().checkDestPlaced() && !data.getGameState()
				.getPlayerHomeGrid().checkSubPlaced());
	}

	public String placeSub(int i, int j) {
		String out = "";
		if (submarineCanBePlaced()) {
			boolean valid;
			if (isShipRotatedHorizonally()) {
				valid = data.getGameState().getPlayerHomeGrid().addSub(i, j, 0);
			} else {
				valid = data.getGameState().getPlayerHomeGrid().addSub(i, j, 1);
			}

			Graphics hp = data.getHomePanel().getGraphics();

			if (valid) {
				if (isShipRotatedHorizonally()) {
					SubmarineH.paint(hp, (j * 20), (i * 20));
				} else {
					Submarine.paint(hp, (j * 20), (i * 20));
				}
				out = out + data.getGameState().getPlayerHomeGrid().toString();
				data.setSubPlaced(true);
				getOutText().setText("Submarine Placed");
			} else {
				out = "not valid";
				out = out + data.getGameState().getPlayerHomeGrid().toString();
				getOutText().setText("Submarine Will Not Fit Here");
			}
		}

		return out;
	}

	private boolean minesweeperCanBePlaced() {
		return (checkAirPlaced() && data.isBattlePlaced() && data.isDestPlaced()
				&& data.isSubPlaced() && !data.isMinePlaced());
	}

	public String placeMine(int i, int j) {
		String out = "";
		if (minesweeperCanBePlaced()) {
			boolean valid;
			if (isShipRotatedHorizonally()) {
				valid = data.getGameState().getPlayerHomeGrid().addMine(i, j, 0);
			} else {
				valid = data.getGameState().getPlayerHomeGrid().addMine(i, j, 1);
			}
			Graphics hp = data.getHomePanel().getGraphics();

			if (valid) {
				if (isShipRotatedHorizonally()) {
					MinesweeperH.paint(hp, (j * 20), (i * 20));
				} else {
					Minesweeper.paint(hp, (j * 20), (i * 20));
				}
				out = out + data.getGameState().getPlayerHomeGrid().toString();
				data.setMinePlaced(true);
				getOutText().setText("Minesweeper Placed");
			} else {
				out = "not valid"
						+ data.getGameState().getPlayerHomeGrid().toString();
				getOutText().setText("Minesweeper Will Not Fit Here");
			}

			if (allShipsPlaced())
				this.endDeploymentPhase();
		}

		return out;
	}

	private boolean allShipsPlaced() {
		return (checkAirPlaced() && data.isBattlePlaced() && data.isDestPlaced()
				&& data.isSubPlaced() && data.isMinePlaced());
	}

	public boolean rotate() {
		setHoriz(!isShipRotatedHorizonally());
		if (isShipRotatedHorizonally()
				&& !data.getGameState().isBothPlayerAndAgentShipsDeployed())
			getOutText().setText("Ship Will Be Placed Horizontally");
		if (!isShipRotatedHorizonally()
				&& !data.getGameState().isBothPlayerAndAgentShipsDeployed())
			getOutText().setText("Ship Will Be Placed Vertically");
		return isShipRotatedHorizonally();
	}

	public void showMap() {
		data.setShowMap(true);
		this.paintMap();
		getOutText().setText("Influence Map shown");
	}

	public void hideMap() {
		data.setShowMap(false);

		Graphics g = data.getInfluenceMapPanel().getGraphics();

		for (int i = 0; i < 10; i++) // change these to ROWS to use the default
		{
			for (int j = 0; j < 10; j++)// change this to CoLumns for default
			{
				int col = 0;
				InfluenceMapGraphic.paint(g, (j * 20), (i * 20), col);
			}
		}

		getOutText().setText("Influence Map Hidden");
	}

	public String deploy(int i, int j) {
		return this.placeAir(i, j) + "\n" + this.placeBattle(i, j) + "\n"
				+ this.placeDest(i, j) + "\n" + this.placeSub(i, j) + "\n"
				+ this.placeMine(i, j) + data.getGameState().isPlayerTurn();

	}

	public void setAgentWins() {
		data.setAgentWins(true);

	}

	public boolean getGameOver() {
		return data.getGameState().IsGameOver();
	}

	public void endDeploymentPhase() {
		if (allShipsPlaced())
			data.getGameState().SetAllShipsDeployed();
		getOutText()
				.setText(
						"All Ships Deployed, Player's Turn! Click on the left grid to fire shots");
		this.data.getGameState().setPlayerTurn();
		data.getOutText().setText(data.getGameState().turnToString());
	}

	public void paintMap() {

		Graphics g = data.getInfluenceMapPanel().getGraphics();

		for (int i = 0; i < 10; i++) // change these to ROWS to use the default
		{
			for (int j = 0; j < 10; j++)// change this to CoLumns for default
			{
				int col = data.getGameState().getInfluenceMap().getValue(i, j);

				if (data.isShowMap()) {
					InfluenceMapGraphic.paint(g, (j * 20), (i * 20), col);
				}
			}
		}

	}

	public void paintPlayerAttackGrid() {
		this.data.getGameState().setShipSunkStates();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (data.getGameState().isCompHomegridRefIsminus3(i, j)
						&& data.getGameState().isAgentAirSunk()) {
					Graphics attackPanelGraphics = data.getAttackPanel()
							.getGraphics();
					SunkIcon.paint(attackPanelGraphics, (j * 20), (i * 20));
				}

				if (data.getGameState().isCompHomeGridRefMinus4(i, j)
						&& data.getGameState().isAgentBattleSunk()) {
					Graphics ap = data.getAttackPanel().getGraphics();
					SunkIcon.paint(ap, (j * 20), (i * 20));
				}

				// (agentMineSunk || agentDestSunk || agentSubSunk ||
				// agentBattleSunk
			}
		}
	}

	private boolean validForShooting() {
		return (data.getGameState().isAgentTurn() && data.getGameState()
				.isBothPlayerAndAgentShipsDeployed());
	}

	private boolean takenShot(int sqrVal) {
		return (sqrVal < 0 || sqrVal == 1);
	}

	public void agentShot(int X, int Y)

	{
		if (validForShooting()) {
			int sqrVal = data.getGameState().getPlayerHomeGrid().getGridValue(X, Y);

			if (takenShot(sqrVal)) {
				System.out.println("Shot already taken! Have another go");
			}

			if (sqrVal == 0) {
				System.out.println(data.getGameState().getPlayerHomeGrid()
						.shot(X, Y));
				data.getGameState().getCompAtt().update(X, Y, 1);
				data.getGameState().getInfluenceMap().miss(X, Y);
				this.paintMap();
				Graphics hp = data.getHomePanel().getGraphics();
				MissIcon.paint(hp, (Y * 20), (X * 20));
				getOutText().setText("Agent Has Missed. Player's Turn");
				this.data.getGameState().setPlayerTurn();
				data.getOutText().setText(data.getGameState().turnToString());
			}

			if (sqrVal > 1) {
				System.out.println(data.getGameState().getPlayerHomeGrid()
						.shot(X, Y));
				data.getGameState().getCompAtt().update(X, Y, 8);
				data.getGameState().getInfluenceMap().hit(X, Y);
				Graphics hp = data.getHomePanel().getGraphics();
				HitIcon.paint(hp, (Y * 20), (X * 20));
				getOutText().setText(
						"Agent Has Hit One Of your ships! Agent's Turn again");
				this.paintMap();

			}

			System.out.println("compAtt");
			System.out.println(data.getGameState().getCompAtt().toString());

		}

		System.out.println("Map is \n"
				+ data.getGameState().getInfluenceMap().toString());

	}

	private void setHoriz(boolean horiz) {
		this.data.setHoriz(horiz);
	}

	private boolean isShipRotatedHorizonally() {
		return data.isHoriz();
	}

	public void setOutText(JTextField outText) {
		this.data.setOutText(outText);
	}

	public JTextField getOutText() {
		return data.getOutText();
	}

	public boolean checkMineSunk() {
		return data.getGameState().getPlayerHomeGrid().checkMineSunk();
	}

	public boolean checkAirSunk() {
		return data.getGameState().getPlayerHomeGrid().checkAirSunk();
	}

	public boolean checkSubSunk() {
		return data.getGameState().getPlayerHomeGrid().checkSubSunk();
	}

	public boolean checkDestSunk() {
		return data.getGameState().getPlayerHomeGrid().checkDestSunk();
	}

	public boolean checkBattleSunk() {
		return data.getGameState().getPlayerHomeGrid().checkBattleSunk();
	}

	public int getGridValue(int i, int j) {
		return data.getGameState().getPlayerHomeGrid().getGridValue(i, j);
	}
}

class RotateButtonAction extends MouseAdapter {

	private GUI gui;

	public RotateButtonAction(GUI gui2) {

		gui = gui2;
	}

	public void mousePressed(MouseEvent event) {
		System.out.println("Horiz = " + gui.rotate());
	}

}

class HideButtonAction extends MouseAdapter {

	private GUI gui;

	public HideButtonAction(GUI gui2) {

		gui = gui2;
	}

	public void mousePressed(MouseEvent event) {
		gui.hideMap();
	}

}

class ShowButtonAction extends MouseAdapter {

	private GUI gui;

	public ShowButtonAction(GUI gui2) {

		gui = gui2;
	}

	public void mousePressed(MouseEvent event) {
		gui.showMap();
	}

}

class NewButtonAction extends MouseAdapter {

	private GUI gui;

	public NewButtonAction(GUI gui2) {

		gui = gui2;
	}

	public void mousePressed(MouseEvent event) {
		gui.reset();
	}

}

class QuitButtonAction extends MouseAdapter {

	public QuitButtonAction() {
	}

	public void mouseClicked(MouseEvent event) {
		System.exit(1);
	}

}
