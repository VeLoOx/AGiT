load("../JavaProject/GameScripts/AlienMaps.js");

var generateAsteroid1 = function(sceneWidth) {
	var ASTEROID = Java.type("pl.agit.Game.Sprites.Characters.Asteroid");
	var ast = new ASTEROID(getRandomInt(5, 20), "red");

	var CIRCLE = Java.type("javafx.scene.shape.Circle");
	var node = ast.getAsNode();

	ast.vX = 0;
	ast.vY = getRandomArbitrary(1, 7);
	newX = getRandomInt(50, (sceneWidth - ast.getRadius()));
	newY = 10;

	node.setTranslateX(newX);
	node.setTranslateY(newY);
	node.setVisible(true);
	// circle.setId(ast.toString());

	return ast;

};



var returnAlienMapList = function (){
	var mapNumbers = 3; // ile ma byc map
	
	return getMapList();
	
}


function returnAlienMap(type) {
	
	getMapList();
	
	if (type == 0) {
		var r = 3;
		var c = 10;
		var tab = JavaMatrix(c, r);

		for (i = 0; i < r; i++) {
			for (x = 0; x < c; x++) {
				tab[i][x] = getRandomInt(0, 1);
			}
		}
		return tab;
	}
}

// tablice javowe
var JavaMatrix = function(columns, rows) {
	var byte2DArrayType = Java.type("byte[][]");
	var byte1DArrayType = Java.type("byte[]");
	var tab2 = new byte2DArrayType(rows);

	for (var i = 0; i < rows; i += 1) {
		tab2[i] = new byte1DArrayType(columns);
	}
	return tab2;

}

// tablice czysto skryptowe
var Matrix = function(columns, rows) {
	this.rows = rows;
	this.columns = columns;
	this.myarray = new Array(this.rows);
	for (var i = 0; i < this.columns; i += 1) {
		this.myarray[i] = new Array(this.rows)
	}
	return this.myarray;
}

function getRandomArbitrary(min, max) {
	return Math.random() * (max - min) + min;
}

function getRandomInt(min, max) {
	return Math.floor(Math.random() * (max - min + 1)) + min;
}