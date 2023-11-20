import javax.swing.JPanel
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.ImageIcon
import java.awt.event.ActionListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.Color
import java.awt.Font
import java.util.Timer
import kotlin.concurrent.schedule

class Play() : JPanel() {

	val background2 = JLabel(ImageIcon("background2.jpg"))
	val board = JLabel(ImageIcon("board.jpg"))
	val draw = JLabel()
	val playerOneList = arrayListOf<JLabel>()
	val playerTwoList = arrayListOf<JLabel>()
	val buttonsList = arrayListOf<JButton>()
	val buttonsText = listOf("Player 2 end turn", "Player 1 end turn", "Back to menu", "Undo last move")

	init {
		setLayout(null)
		background2.setBounds(0, 0, 600, 700)
		board.setBounds(50, 80, 500, 500)
		add(background2)
	}
	
	fun createLabels() {
	
		for (l in 0..11) {
			val rock2 = JLabel(ImageIcon("rock2.png"))
			rock2.setSize(68, 70)
			Process.player2Index.add(l)
			playerTwoList.add(rock2)
			rock2.setLocation(Process.xANDy(l, Process.player2Index))
			if (Process.userNumber == 2) {
				rock2.addMouseMotionListener(DragMouseAdapter(playerTwoList, l, 2))
				rock2.addMouseListener(ClickMouseAdapter(playerTwoList,
														 playerOneList,
														 Process.player2Index,
														 Process.player1Index,
														 l, 2, this))
			}
			board.add(rock2)
		}
	
		for (l in 13..24) {
			val rock1 = JLabel(ImageIcon("rock1.png"))
			rock1.setSize(68, 70)
			Process.player1Index.add(l)
			playerOneList.add(rock1)
			rock1.setLocation(Process.xANDy(l-13, Process.player1Index))
			rock1.addMouseMotionListener(DragMouseAdapter(playerOneList, l-13, 1))
			rock1.addMouseListener(ClickMouseAdapter(playerOneList,
													 playerTwoList,
													 Process.player1Index,
													 Process.player2Index,
													 l-13, 1, this))
			board.add(rock1)
		}
		background2.add(board)
		
		for (b in 0..3) {
			var button = JButton(buttonsText[b])
			button.setFocusPainted(false)
			button.addActionListener({userChoice(b)})
			button.setBackground(Color(22, 29, 37))
			button.setForeground(Color(125, 137, 162))
			background2.add(button)
			buttonsList.add(button)
		}
		buttonsList[0].setBounds(200, 30, 200, 30)// Player 2
		buttonsList[0].setEnabled(false)
		buttonsList[1].setBounds(200, 600, 200, 30)// Player 1
		buttonsList[1].setEnabled(false)
		buttonsList[2].setBounds(75, 650, 200, 30)// back
		buttonsList[3].setBounds(325, 650, 200, 30)// undo
		buttonsList[3].setEnabled(false)
		
		draw.setBounds(10, 0, 50, 70)
		draw.setFont(Font("Arial", Font.BOLD, 35))
		draw.setForeground(Color.red)
		background2.add(draw)
	}
	
	fun reset() {
	
		playerOneList.clear()
		playerTwoList.clear()
		buttonsList.clear()
		background2.removeAll()
		board.removeAll()
	}
	
	fun userChoice(indx: Int) {
	
		if (indx == 1) {
			buttonsList[1].setEnabled(false)
			buttonsList[3].setEnabled(false)
			Process.jumpList1.clear()
			Process.moveList.clear()
			Process.lastJump = false
			Process.player1Turn = false
			if (Process.userNumber == 1) {
				Timer().schedule(200) {
					var cpu = Process.cpuChoice(-2)
					animation(cpu[0], cpu[1])
				}
			}
			else Process.player2Turn = true
			
			var player1Set = Process.player1Index.toSet()
			if (player1Set.size <= 5)  {
				Process.countDown -= 1
				draw.setText(Process.countDown.toString())
				if (Process.countDown == 0) {
					Process.winner = "Draw!"
					Jump.control("Show result")
				}
			}
		}
		if (indx == 0) {
			buttonsList[0].setEnabled(false)
			buttonsList[3].setEnabled(false)
			Process.jumpList1.clear()
			Process.moveList.clear()
			Process.lastJump = false
			Process.player2Turn = false
			Process.player1Turn = true
			
			var player2Set = Process.player2Index.toSet()
			if (player2Set.size <= 5)  {
				Process.countDown -= 1
				draw.setText(Process.countDown.toString())
				if (Process.countDown == 0) {
					Process.winner = "Draw!"
					Jump.control("Show result")
				}
			}
		}
		
		if (indx == 2) {
			Process.reset()
			Jump.control("Back to menu")
		}
		if (indx == 3) {
			buttonsList[3].setEnabled(false)
			buttonsList[1].setEnabled(false)
			buttonsList[0].setEnabled(false)
			if (Process.player1Turn) {
				if (Process.jumpList1[0] == 12) {
					Process.player1Index[Process.jumpList1[1]] = Process.moveList[0][Process.jumpList1[1]]
					playerOneList[Process.jumpList1[1]].setLocation(Process.xANDy(Process.jumpList1[1], Process.player1Index))
					Process.jumpList1.clear()
				}
				else {
					Process.player1Index[Process.jumpList1[0]] = Process.moveList[0][Process.jumpList1[0]]
					playerOneList[Process.jumpList1[0]].setLocation(Process.xANDy(Process.jumpList1[0], Process.player1Index))
					Process.player2Index[Process.jumpList1[1]] = Process.moveList[1][Process.jumpList1[1]]
					playerTwoList[Process.jumpList1[1]].setVisible(true)
					Process.lastJump = false
				}
			}
			if (Process.player2Turn) {
				if (Process.jumpList1[0] == 12) {
					Process.player2Index[Process.jumpList1[1]] = Process.moveList[0][Process.jumpList1[1]]
					playerTwoList[Process.jumpList1[1]].setLocation(Process.xANDy(Process.jumpList1[1], Process.player2Index))
					Process.jumpList1.clear()
				}
				else {
					Process.player2Index[Process.jumpList1[0]] = Process.moveList[0][Process.jumpList1[0]]
					playerTwoList[Process.jumpList1[0]].setLocation(Process.xANDy(Process.jumpList1[0], Process.player2Index))
					Process.player1Index[Process.jumpList1[1]] = Process.moveList[1][Process.jumpList1[1]]
					playerOneList[Process.jumpList1[1]].setVisible(true)
					Process.lastJump = false
				}
			}
			Process.jumpList.clear()
			//Process.jumpList1.clear()
			Process.moveList.clear()
			board.revalidate()
			board.repaint()
		}
	}
	
	fun animation(n: Int, rXY: Int) {
	
		for (p in playerTwoList) {
			board.remove(p)
		}
		for (a in playerOneList) {
			board.remove(a)
		}
		
		board.add(playerTwoList[n])
		
		for (a in playerOneList) {
			board.add(a)
		}
		for (p in playerTwoList) {
			if (p == playerTwoList[n]) continue
			board.add(p)
		}
		
		board.revalidate()
		board.repaint()
		
		var rX = Process.xANDy(rXY, Process.flatIndex).getX().toInt()
		var rY = Process.xANDy(rXY, Process.flatIndex).getY().toInt()
		
		var lX = playerTwoList[n].getX()
		var lY = playerTwoList[n].getY()
		
		for (i in 1..200) {
			if (lX == rX && lY == rY) break
			Timer().schedule((i*5).toLong()) {
				if (lX < rX) lX++
				if (lX > rX) lX--
				if (lY < rY) lY++
				if (lY > rY) lY--
				playerTwoList[n].setLocation(lX, lY)
				board.revalidate()
				board.repaint()
			} 
		}
		Timer().schedule(1100) {
			Process.changePlayerValue(n, Process.player2Index, lX, lY)
			var isJump = false
			if (!Process.jumpList2.isEmpty()) {
				for (j in Process.jumpList2) {
					if (Process.player2Index[n] == j[1] && n == j[2]) {
						isJump = true
						Process.player1Index[j[0]] = -1
						Timer().schedule(300) {
							playerOneList[j[0]].setVisible(false)
							board.revalidate()
							board.repaint()
						}
					}
				}
				if (Process.player2Index.max() > -1 && isJump) {
					Timer().schedule(400) {
						var cpu = Process.cpuChoice(n)
						if (!cpu.isEmpty() && cpu[0] == n) {
							animation(cpu[0], cpu[1])
						}
					}
				}
			}
			Timer().schedule(700) {
				Process.player1Turn = true
				Process.jumpList1.clear()
				Process.moveList.clear()
				if (Process.player1Index.max() == -1) {
					for (button in buttonsList) button.setEnabled(false)
					Process.winner = "Player 2 win!"
					Jump.control("Show result")
				}
				else {
					var player2Set = Process.player2Index.toSet()
					if (player2Set.size <= 5)  {
						Process.countDown -= 1
						draw.setText(Process.countDown.toString())
						if (Process.countDown == 0) {
							Process.winner = "Draw!"
							Jump.control("Show result")
						}
					}
				}
			}
		}
	}
	
	fun changeLocations(n: Int,
						l1: ArrayList<JLabel>,
						l2: ArrayList<JLabel>,
						p1: ArrayList<Int>,
						p2: ArrayList<Int>) {

		if (Process.moveList.isEmpty() || Process.lastJump) {
			Process.moveList.clear()
			Process.moveList += ArrayList(p1)
			Process.moveList += ArrayList(p2)
		}
		
		fun undo() {
		
			p1[n] = Process.moveList[0][n]
			l1[n].setLocation(Process.xANDy(n, p1))
			board.revalidate()
			board.repaint()
			//if (!Process.jumpList1.isEmpty()) {
				//buttonsList[3].setEnabled(false)
			//}
		}
		
		fun endTurn() {
		
			if (p1 == Process.player1Index) {
				buttonsList[1].setEnabled(true)
				if (Process.player2Index.max() == -1) {
					for (button in buttonsList) button.setEnabled(false)
					Process.winner = "Player 1 win!"
					Jump.control("Show result")
				}
			}
			else {
				buttonsList[0].setEnabled(true)
				if (Process.player1Index.max() == -1) {
					for (button in buttonsList) button.setEnabled(false)
					Process.winner = "Player 2 win!"
					Jump.control("Show result")
				}
			}
			buttonsList[3].setEnabled(true)
		}
		
		val arrayX = Array(5) {i -> i*100+16}
		val arrayY = Array(5) {i -> i*100+14}
		if (Process.dragX > 416) Process.dragX = 416
		else {
			for (arr in arrayX) {
				if ((Process.dragX+50) <= (arr+100)) {
					Process.dragX = arr
					break
				}
			}
		}
		if (Process.dragY > 414) Process.dragY = 414
		else {
			for (arr in arrayY) {
				if ((Process.dragY+50) <= (arr+100)) {
					Process.dragY = arr
					break
				}
			}
		}
		l1[n].setLocation(Process.dragX, Process.dragY)
		board.revalidate()
		board.repaint()
		if (Process.illegalMove(n, p1, p2)) {
			undo()
			return
		}
		Process.changePlayerValue(n, p1, Process.dragX, Process.dragY)
		if (!Process.jumpList1.isEmpty()) {
			if (n != Process.jumpList1[0] || Process.jumpList.isEmpty()) {
				undo()
				return
			}
			else if (!Process.jumpList.isEmpty()) {
				var validJump = false
				for (j in Process.jumpList) {
					if (p1[n] == j[1] && n == j[2]) validJump = true
				}
				if (!validJump) {
					undo()
					return
				}
			}
		}
		if (!Process.jumpList.isEmpty()) {
			for (j in Process.jumpList) {
				if (p1[n] == j[1] && n == j[2]) {
					Process.jumpList1.clear()
					Process.jumpList1.add(p1.indexOf(j[1]))
					Process.jumpList1.add(j[0])
					Process.lastJump = true
					Timer().schedule(300) {
						l2[j[0]].setVisible(false)
						board.revalidate()
						board.repaint()
						p2[j[0]] = -1
						endTurn()
					}
				}
			}
			return
		}
		Process.jumpList1.add(12)
		Process.jumpList1.add(n)
		endTurn()
	}
}

class DragMouseAdapter(val lList: ArrayList<JLabel>,
					   val ind: Int,
					   val bool: Int) : MouseAdapter() {
	
	override fun mouseDragged(e: MouseEvent) {
	
		if (!Process.turns(bool)) return
		
		Process.dragX = lList[ind].getX() + e.getX()
		Process.dragY = lList[ind].getY() + e.getY()
		lList[ind].setLocation(Process.dragX - 30, Process.dragY - 30)
	}
}

class ClickMouseAdapter(val lList1: ArrayList<JLabel>,
				 		val lList2: ArrayList<JLabel>,
				 		val pList1: ArrayList<Int>,
				 		val pList2: ArrayList<Int>,
				 		val indx: Int,
				 		val bool: Int,
				 		val grandParent: Play) : MouseAdapter() {

	override fun mouseEntered(e: MouseEvent) {
		
		if (!Process.turns(bool)) return
		
		val parent = lList1[indx].getParent()
		
		for (p in lList2) {
			parent.remove(p)
		}
		for (a in lList1) {
			parent.remove(a)
		}
		
		parent.add(lList1[indx])
		
		for (a in lList1) {
			if (a == lList1[indx]) continue
			parent.add(a)
		}
		for (p in lList2) {
			parent.add(p)
		}
		
		parent.revalidate()
		parent.repaint()
	}

	override fun mouseReleased(e: MouseEvent) {

		if (!Process.turns(bool)) return
		grandParent.changeLocations(indx, lList1, lList2, pList1, pList2)
	}
}
