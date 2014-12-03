
var generateAsteroid1 = function(sceneWidth){
	var ASTEROID = Java.type("pl.agit.Game.Sprites.Characters.Asteroid");
	var ast = new ASTEROID(getRandomInt(5, 21),"red");
	
	var CIRCLE = Java.type("javafx.scene.shape.Circle");
	var node = ast.getAsNode();
	
	ast.vX = 0;
	ast.vY = getRandomArbitrary(1, 7);
	newX = getRandomInt(50, (sceneWidth - ast.getRadius()));
	newY = 10;

	node.setTranslateX(newX);
	node.setTranslateY(newY);
	node.setVisible(true);
//	circle.setId(ast.toString());
	
	return ast;
	
};

//var loadAlienMap = function(asw, maxCol, maxRow, startx,starty,interPoz, imageWidth,imageHeight){
//	
//	var SpriteArr = Java.type("pl.agit.Game.Sprites.Sprite[]");
//	var tab = returnAlienMap(0);
//	
//	var ALIEN = Java.type("pl.agit.Game.Sprites.Characters.AlienShip");
//	
//	for(c=0;c<tab.length;c++){
//		for (r=0;r<tab[c].length;r++){
//			if(tab[c][r]==1){
//				var ali = new ALIEN(startx+(r*imageWidth)+interPoz, starty+(c*imageHeight)+interPoz);
//				ali.vX=3;
//				ali.node.toFront();
//				//SpriteArr = [ali];
//				asw.getSpriteManager().addSprite(ali);
//				asw.getSceneElements().getChildren().add(ali.getNode());
//			}
//			}
//		}
//	
//}

function returnAlienMap(){
	var r=3;
	var c=10;
	var tab = Matrix(c,r);
	//var tab2 = Java.type("byte[][]");
	
		for(i=0;i<r;i++){
			for(x=0;x<c;x++){
				tab[i][x]=getRandomInt(0, 1);
			}
		}
		
	
	return Java.to(tab,"byte[][]");
}

var Matrix = function (columns, rows)  {
    this.rows = rows;
    this.columns = columns;
    this.myarray = new Array(this.rows);
    for (var i=0; i < this.columns; i +=1) {
        this.myarray[i]=new Array(this.rows)
    }
    return this.myarray;
}

function getRandomArbitrary(min, max) {
    return Math.random() * (max - min) + min;
}

function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}