

var generateShatter = function(pozx, pozy, vY, bigAstRadius){
	
	var ASTEROID = Java.type("pl.agit.Game.Sprites.Characters.Asteroid");
	var ASTEROID1DARRAY = Java.type("pl.agit.Game.Sprites.Characters.Asteroid[]");
	newMaxRadius = bigAstRadius/2;
	newMinRadius = bigAstRadius/3;
	
	
	shatterNumb = getRandomInt(1, 4);
	astTab = new ASTEROID1DARRAY(shatterNumb);
	
	for(i=0;i<shatterNumb;i++){
		
		rad = getRandomInt(newMaxRadius, newMinRadius)+1;
				
		var ast = new ASTEROID(rad, "red");
		direct=1; //1=na prawo  -1 = na lewo
		interPozX=getRandomInt(50, 130);
		if(getRandomInt(0, 1)==1) direct = -1;
		
		interPozX = interPozX*direct;
		ast.getNode().setTranslateX(pozx+interPozX);
		ast.getNode().setTranslateY(pozy+getRandomInt(-1, -10));
		ast.vY = vY+getRandomInt(2, 5);
		ast.vX = 0
		if(getRandomInt(0,3)==3) {
			ast.vX = getRandomInt(-4,2)+1;
		}
		
		astTab[i] = ast;		
	}
	
	return astTab;
	
}



function getRandomArbitrary(min, max) {
	return Math.random() * (max - min) + min;
}

function getRandomInt(min, max) {
	return Math.floor(Math.random() * (max - min + 1)) + min;
}