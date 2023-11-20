import javax.swing.JPanel
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.ImageIcon
import java.awt.Font
import java.awt.Color
import java.awt.event.ActionListener
import java.util.Timer
import kotlin.concurrent.schedule

class Menu() : JPanel() {

	val background = JLabel(ImageIcon("background1.jpg"))
	val paw = JLabel(ImageIcon("paw.png"))
	val buttons = arrayListOf<JButton>()
	val buttonText = listOf("One player", "Two player", "Exit", "Easy", "Normal", "Back")

	init {
		setLayout(null)
		background.setBounds(0, 0, 600, 700)
		paw.setBounds(50, 100, 500, 500)
		add(background)
		background.add(paw)
		createMenu(0)
	}
	
	fun createMenu(m: Int) {
	
		buttons.clear()
		paw.removeAll()
		paw.revalidate()
		for (b in 0..2) {
			Timer().schedule(((b+1)*500).toLong()) {
				var button = JButton(buttonText[b+m])
				button.addActionListener({choices(b+m)})
				button.setFocusPainted(false)
				button.setBorderPainted(false)
				button.setBounds(150, b*80+140, 200, 50)
				button.setFont(Font("Arial", Font.ITALIC, 30))
				button.setForeground(Color.black)
				button.setBackground(Color(115, 115, 115))
				buttons.add(button)
				paw.add(button)
				paw.repaint()
			}
		}
	}
	
	fun choices(c: Int) {
	
		if (c == 0) {
			createMenu(3)
			Process.userNumber = 1
		}
		if (c == 1) {
			Process.userNumber = 2
			Jump.control("Start play")
		}
		if (c == 2) Jump.control("exit")
		if (c == 3) {
			Process.difficulty = 1
			Jump.control("Start play")
		}
		if (c == 4) {
			Process.difficulty = 2
			Jump.control("Start play")
		}
		if (c == 5) createMenu(0)
	}
}
