import javax.swing.JPanel
import javax.swing.JDialog
import javax.swing.JButton
import javax.swing.JLabel
import java.awt.event.ActionListener
import java.awt.Color
import java.awt.Font


class Result(parent: Jump) : JDialog() {

	val panel = JPanel()
	val label =JLabel()
	val buttonsList = arrayListOf<JButton>()
	
	init {
		setSize(220, 220)
		setResizable(false)
		setUndecorated(true)
		defaultCloseOperation = DO_NOTHING_ON_CLOSE
		setLocationRelativeTo(parent)
		panel.setLayout(null)
		panel.add(label)
		add(panel)
	}
	
	fun showDialog() {
	
		label.setBounds(0, 0, 220, 220)
		label.setOpaque(true)
		label.setBackground(Color(22, 29, 37))
		for (b in 0..2) {
			val button = JButton()
			button.setFocusPainted(false)
			button.addActionListener({userMenu(b)})
			button.setBackground(Color(125, 137, 162))
			button.setForeground(Color(22, 29, 37))
			label.add(button)
			buttonsList.add(button)
		}
		var textLabel = JLabel()
		textLabel.setText(Process.winner)
		textLabel.setBounds(10, 20, 200, 30)
		textLabel.setFont(Font("Arial", Font.ITALIC, 30))
		textLabel.setForeground(Color(125, 137, 162))
		label.add(textLabel)
		buttonsList[0].setText("Play again")
		buttonsList[0].setBounds(10, 70, 200, 30)
		buttonsList[1].setText("Back to menu")
		buttonsList[1].setBounds(10, 120, 200, 30)
		buttonsList[2].setText("Exit")
		buttonsList[2].setBounds(10, 170, 200, 30)
		
		revalidate()
		repaint()
		setModal(true)
		setVisible(true)
	}
	
	fun userMenu(n: Int) {
	
		if (n == 0) {
			Process.reset()
			Jump.control("Start play")
		}
		if (n == 1) {
			Process.reset()
			Jump.control("Back to menu")
		}
		if (n == 2) {
			Jump.control("exit")
		}
		
		reset()
	}
	
	fun reset() {
	
		label.removeAll()
		buttonsList.clear()
		setVisible(false)
	}
}
