/**
 * Scripts for alien space ship
 */

var behavior1 = function (as,speedCell){
	/*Kamikaze behavior   - as = alien ship
	 * 
	 * Statki lataja od lewej do prawej, i lososwo zlatuja w strone gracza
	 * */
	var atack = 0;
	if (!Boolean(atack))
		as.refreshView(as.getCellClips()[2]);
	else {
		as.refreshView();
	}
	
	as.node.setTranslateX(as.node.getTranslateX() + as.vX);
	as.node.setTranslateY(as.node.getTranslateY() + as.vY);
	
		
	if (as.node.getTranslateX() >= as.scW || as.node.getTranslateX() <= 20)
		as.vX = -as.vX;

	var howLong = 300; // jak g³êboko ma zaatakowac

	if (!Boolean(atack)) {
		
		if (getRandomInt(0, 1000) == 1) {
			atack = 1; //true
			as.vY = 10;
		}
	}
	
	if (as.node.getTranslateY() >= as.starty + howLong)
		as.vY = -as.vY;

	if (as.node.getTranslateY() <= as.starty) {
		as.node.setTranslateY(as.starty);
		as.refreshView();
		atack = 0; //false
	}
	
};


function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}