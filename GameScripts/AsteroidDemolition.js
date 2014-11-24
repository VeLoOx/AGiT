
var generateAsteroid1 = function(sceneWidth){
	var ASTEROID = Java.type("pl.agit.Game.Sprites.Characters.Asteroid");
	var ast = new ASTEROID(getRandomInt(5, 10),"red");
	
	var CIRCLE = Java.type("javafx.scene.shape.Circle");
	var circle = ast.getAsCircle();
	
	ast.vX = 0;
	ast.vY = getRandomArbitrary(1, 7);
	newX = getRandomInt(circle.getRadius(), (sceneWidth - circle.getRadius()));
	newY = 10;

	circle.setTranslateX(newX);
	circle.setTranslateY(newY);
	circle.setVisible(true);
	circle.setId(ast.toString());
	
	return ast;
	
};

function getRandomArbitrary(min, max) {
    return Math.random() * (max - min) + min;
}

function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}