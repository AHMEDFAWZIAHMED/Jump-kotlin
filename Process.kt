import java.awt.Point

object Process {

	val locations = (0..4).map {row -> (0..4).map {
						col -> (1 downTo 0).map {
						item -> if(item==0) row*100+14 else col*100+16}}}
	val locIndex = (0..24).map {outer -> (0..1).map {
					iner -> if(outer<5) iner*outer else if(
					iner==0) outer/5 else outer%5}}
	var player1Index = arrayListOf<Int>()
	var player2Index = arrayListOf<Int>()
	val flatIndex = arrayListOf<Int>(*Array(25) {it*1})
	val rowIndex = (0..4).map {row -> (0..4).map {row*5+it}}
	val colIndex = (0..4).map {col -> (0..4).map {it*5+col}}
	var jumpList = arrayListOf<List<Int>>()
	var jumpList1 = arrayListOf<Int>()
	var jumpList2 = arrayListOf<List<Int>>()
	var moveList = arrayListOf<List<Int>>()
	var targeted = arrayListOf<Int>()
	var dragX = 0
	var dragY = 0
	var lastJump = false
	var countDown = 30
	var player1Turn = true
	var player2Turn = false
	var userNumber = 2
	var difficulty = 1
	var winner = ""
	
	fun reset() {
	
		player1Index.clear()
		player2Index.clear()
		jumpList.clear()
		jumpList1.clear()
		jumpList2.clear()
		moveList.clear()
		targeted.clear()
		player1Turn = true
		player2Turn = false
		lastJump = false
		countDown = 30
	}
	
	fun turns(num: Int) : Boolean {
	
		if(num == 1) return player1Turn
		return player2Turn
		//return player1Turn if(num == 1) else player2Turn
		//return if(num == 1) player1Turn else player2Turn
	}
	
	fun findNearNeighbors(number: Int): ArrayList<Int> {
	
		var allInd = listOf(-1, 1, -5, 5).map {it+number}
		var firstCo = listOf(1, -5, 5).map {it+number}
		var lastCo = listOf(-1, -5, 5).map {it+number}
		var nearNeighbors = arrayListOf<Int>()
		
		if (number in colIndex[0]) {
			for (f in firstCo) {
				if (f in flatIndex) nearNeighbors += f
			}
		}
		else if (number in colIndex[4]) {
			for (l in lastCo) {
				if (l in flatIndex) nearNeighbors += l
			}
		}
		else {
			for (a in allInd) {
				if (a in flatIndex) nearNeighbors += a
			}
		}
		
		return nearNeighbors
	}
	
	fun findFarNeighbors(number: Int): ArrayList<Int> {
	
		var allInd = listOf(-2, 2, -10, 10).map {it+number}
		var firstTwoCo = listOf(2, -10, 10).map {it+number}
		var lastTwoCo = listOf(-2, -10, 10).map {it+number}
		var farNeighbors = arrayListOf<Int>()
		
		if (number in colIndex[0] || number in colIndex[1]) {
			for (f in firstTwoCo) {
				if (f in flatIndex) farNeighbors += f
			}
		}
		else if (number in colIndex[3] || number in colIndex[4]) {
			for (l in lastTwoCo) {
				if (l in flatIndex) farNeighbors += l
			}
		}
		else {
			for (a in allInd) {
				if (a in flatIndex) farNeighbors += a
			}
		}
		
		return farNeighbors
	}
	
	fun changePlayerValue(num: Int, p: ArrayList<Int>, nX: Int, nY: Int) {
	
		for (i in 0..24) {
			if (Point(nX, nY) == xANDy(i, flatIndex)) {
				p[num] = i
			}
		}
	}
	
	fun illegalMove(num: Int, l1: ArrayList<Int>, l2: ArrayList<Int>) : Boolean {
	
		jumpList.clear()
		return (Point(dragX, dragY) !in possibleMoves(num, l1, l2)
				&& Point(dragX, dragY) !in possibleJump(num, l1, l2))
	}
	
	fun possibleMoves(num: Int, l1: ArrayList<Int>,
						l2: ArrayList<Int>) : ArrayList<Point> {
	
		var location = ArrayList<Point>()
		for (neighbor in findNearNeighbors(l1[num])) {
			if (neighbor !in l1 && neighbor !in l2) {
				location.add(xANDy(neighbor, flatIndex))
			}
		}
		
		return location
	}
	
	fun possibleJump(num: Int, l1: ArrayList<Int>, l2: ArrayList<Int>
					) : ArrayList<Point> {
	
		var location = ArrayList<Point>()
		for (n in findNearNeighbors(l1[num])) {
			if (n in l1 || n !in l2) continue
			for (f in findFarNeighbors(l1[num])) {
				if (f in l1 || f in l2) continue
				if (f==n-1||f==n+1||f==n-5||f==n+5) {
					location.add(xANDy(f, flatIndex))
					jumpList.add(listOf(l2.indexOf(n), f, num))
				}
			}
		}
		return location
	}
					
	fun xANDy(num: Int, l: ArrayList<Int>) : Point {
	
		var pX = locations[locIndex[l[num]][0]][locIndex[l[num]][1]][0]
		var pY = locations[locIndex[l[num]][0]][locIndex[l[num]][1]][1]
		
		return Point(pX, pY)
	}
	
	fun pointToIndex(p: Point) : Int {
	
		var indx = 0
		for (i in 0..4) {
			for (j in 0..4) {
				if (locations[i][j] == listOf(p.getX().toInt(), p.getY().toInt())) {
					indx = locIndex.indexOf(listOf(i, j))
					break
				}
			}
		}
		
		return indx
	}
	
	fun value(ind: Int, nu: Int, num: Int) : Int {
	
		var player1Copy = ArrayList(player1Index)
		var player2Copy = ArrayList(player2Index)
		var number = 0
		var index = -1
		var loc = -1
	
		if (num > -1) player1Copy[num] = -1
		player2Copy[ind] = nu
		for (n in findNearNeighbors(nu)) {
			if (n in player1Copy) {
				jumpList.clear()
				possibleJump(player1Copy.indexOf(n),player1Copy, player2Copy)
				if (!jumpList.isEmpty()) {
					for (jum in jumpList) {
						if (player2Copy[ind] == player2Copy[jum[0]]) {
							number -= 3
							player2Copy[ind] = -1
							player1Copy[jum[2]] = jum[1]
							index = jum[2]
							loc = jum[1]
						}
					}
				}
			}
		}
		findTarget(player1Copy, player2Copy)
		if (!targeted.isEmpty()) number -= 4
		
		if (index >= 0) {
			for (n in findNearNeighbors(loc)) {
				if (n in player2Copy) {
					jumpList.clear()
					possibleJump(player2Copy.indexOf(n),player2Copy, player1Copy)
					if (!jumpList.isEmpty()) {
						for (jum in jumpList) {
							if (player1Copy[index] == player1Copy[jum[0]]) {
								number += 3
								player1Copy[index] = -1
							}
						}
					}
				}
			}
			findTarget(player1Copy, player2Copy)
			if (!targeted.isEmpty()) number -= 4
		}
		//jumpList.clear()
		//println(targeted)
		return number
	}
	
	fun findTarget(l1: ArrayList<Int>, l2: ArrayList<Int>) {
	
		targeted.clear()
		for (i in 0..11) {
			if (l1[i] == -1) continue
			for (j in 0..24) {
				dragX = xANDy(j, flatIndex).getX().toInt()
				dragY = xANDy(j, flatIndex).getY().toInt()
				if (!illegalMove(i, l1, l2)) {
					if (!jumpList.isEmpty()) {
						for (jum in jumpList) {
							targeted.add(jum[0])
						}
					}
				}
			}
		}
	}
	
	fun cpuChoice(num: Int) : ArrayList<Int> {
	
		var finalResult = arrayListOf<Int>()
		var maxIndex = arrayListOf<Int>()
		var maxValues = arrayListOf<Int>()
		var indexList = arrayListOf<List<Int>>()
		var valueList = Array(12) {Array(8) {-2}}
		
		repeat(12) {indexList.add(emptyList())}
		jumpList2.clear()
		
		for (i in 0..11) {
			var pMJ = -1
			if (player2Index[i] == -1) continue
			for (j in 0..24) {
				findTarget(player1Index, player2Index)
				dragX = xANDy(j, flatIndex).getX().toInt()
				dragY = xANDy(j, flatIndex).getY().toInt()
				if (illegalMove(i, player2Index, player1Index)) continue
				if (jumpList.isEmpty() && num == -2) {
					for (p in possibleMoves(i, player2Index, player1Index)) {
						if (pointToIndex(p) !in indexList[i]) {
							pMJ++
							indexList[i] += listOf(pointToIndex(p))
							valueList[i][pMJ] += 6
							if (i in targeted) valueList[i][pMJ] += 3
							if (pointToIndex(p) == player2Index[i]+5) valueList[i][pMJ] += 1
							if (difficulty == 2) {
								valueList[i][pMJ] += value(i, pointToIndex(p), -1)
							}
						}
					}
					continue
				}
				var jumpListCopy = ArrayList(jumpList)
				for (jum in jumpListCopy) {
					if (i == jum[2]) {
						pMJ++
						indexList[i] += listOf(jum[1])
						valueList[i][pMJ] += 10
						if (i in targeted) valueList[i][pMJ] += 5
						if (difficulty == 2) {
							valueList[i][pMJ] += value(i, jum[1], jum[0])
						}
						jumpList2.add(jum)
					}
				}
			}
		}
		for (v in valueList.indices) {
			maxValues.add(valueList[v].max())
			maxIndex.add(valueList[v].indexOf(valueList[v].max()))
		}
		var maxValue = maxValues.lastIndexOf(maxValues.max())
		var notEmpty = false
		for (indexItem in indexList) {
			if (!indexItem.isEmpty()) {
				notEmpty = true
			}
		}
		if (notEmpty) {
			finalResult.add(maxValue)
			finalResult.add(indexList[maxValue][maxIndex[maxValue]])
		}
		return finalResult
	}
}
