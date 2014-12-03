/**
 * Scripts for alien space ship
 */

var behavior1 = function(as,startx,starty,sceneWidth) {
	/*
	 * Kamikaze behavior - as = alien ship
	 * 
	 * Statki lataja od lewej do prawej, i lososwo zlatuja w strone gracza
	 * as-satatek
	 * startx i starty = pozycje startowe
	 * sceneWidth - szerokoscporuszania
	 * changedView - znacznik zmiany stanu widoku
	 */

	as.node.setTranslateX(as.node.getTranslateX() + as.vX);
	as.node.setTranslateY(as.node.getTranslateY() + as.vY);

	if (as.node.getTranslateX() >= sceneWidth || as.node.getTranslateX() <= 20)
		as.vX = -as.vX;

	var howLong = 300; // jak g³êboko ma zaatakowac

	if (as.atack == 0) { // jezeli nie atakuje

		if (getRandomInt(0, 1000) == 1) {
			as.atack = 1; // true
			as.vY = 10;
			as.changedView=1;
			return;
		}
	}
	
		if (as.node.getTranslateY() >= starty + howLong){
			as.vY = -as.vY; //cofnicie z ataku
			return;
		}
		
		if ((as.vY<0) && (as.node.getTranslateY() < starty)) { //wylaczenie z ataku
			as.node.setTranslateY(starty);
			as.atack = 0;
			as.changedView=1;
		}

	

};

function getRandomInt(min, max) {
	return Math.floor(Math.random() * (max - min + 1)) + min;
}