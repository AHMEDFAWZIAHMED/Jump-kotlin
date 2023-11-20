import javax.swing.JFrame
import java.awt.BorderLayout

class Jump() : JFrame() {

	val menu = Menu()
	val play = Play()
	val result = Result(this)

	init {
		startGame()
	}
	
	fun startGame() {
	
		//setTitle("Jump")
		//setUndecorated(true)
		defaultCloseOperation = DO_NOTHING_ON_CLOSE
		setDefaultLookAndFeelDecorated(true)
		setLayout(BorderLayout())
		add(menu, BorderLayout.CENTER)
		//add(play, BorderLayout.CENTER)
		//setLocationRelativeTo(null)
		setSize(600, 735)
		setResizable(false)
	}
	
	companion object {
	
		val jump = Jump()
	
		@JvmStatic
		fun main(args: Array<String>) {
		
			jump.setVisible(true)
		}
		
		fun control(word: String) {
	
			if (word == "Start play") {
				jump.remove(jump.menu)
				jump.play.reset()
				jump.play.createLabels()
				jump.add(jump.play, BorderLayout.CENTER)
			}
			if (word == "Back to menu") {
				jump.remove(jump.play)
				jump.menu.createMenu(0)
				jump.add(jump.menu, BorderLayout.CENTER)
			}
			jump.revalidate()
			jump.repaint()
			
			if (word == "Show result") {
				jump.result.showDialog()
			}
			if (word == "exit") {
				System.exit(0)
			}
		}
	}
}
