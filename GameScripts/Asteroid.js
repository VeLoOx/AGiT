

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

var generateShatter2 = function(pozx, pozy, vY, bigAstRadius,ast){
	
	
	newMaxRadius = bigAstRadius/2;
	newMinRadius = bigAstRadius/3;
	
	
		rad = getRandomInt(newMaxRadius, newMinRadius)+1;
				
		
		direct=1; //1=na prawo  -1 = na lewo
		interPozX=getRandomInt(50, 130);
		if(getRandomInt(0, 1)==1) direct = -1;
		
		interPozX = interPozX*direct;
		ast.getNode().setTranslateX(pozx+interPozX);
		ast.getNode().setTranslateY(pozy+getRandomInt(-5, -30));
		ast.vY = getRandomInt(2, 7);
		ast.vX = getRandomInt(-3, 3);
		
		ast.getNode().setVisible(true);

}


var getRandNumbShatter = function(){
	var i = getRandomInt(3,7);
	
	return i;
}


function getRandomArbitrary(min, max) {
	return Math.random() * (max - min) + min;
}

function getRandomInt(min, max) {
	return Math.floor(Math.random() * (max - min + 1)) + min;
}