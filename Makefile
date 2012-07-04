SOURCES=	src/driver/SpaceInvaders.java \
		src/animator/Animator.java \
		src/animator/AnimatedElement.java \
		src/game/Alien.java \
		src/game/AlienChoreographer.java \
		src/game/Game.java \
		src/game/Phaser.java \
		src/game/Powerup.java \
		src/game/Shell.java \
		src/game/Tank.java \
		src/gui/Controller.java \
		src/gui/Display.java

############## COMPILE COMMANDS ##################

compile:
	javac -d bin $(SOURCES)

todo:
	grep --color=auto -nH -F "TODO" $(SOURCES)
	grep --color=auto -nH -F "left off here" $(SOURCES)

size:
	wc $(SOURCES)

clean:
	rm -f -r bin/*

run:
	cd bin ; java driver.SpaceInvaders ; cd ..

