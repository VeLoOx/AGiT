/**
 * Scripts for main space ship
 */

var reduceEnergy = function (energy, damage){
	return energy-1*damage;
};

var fire = function (shipActualX,shipActualY,shipWidth){
	var MISSILE = Java.type("pl.agit.Game.Sprites.Characters.Missile");
	var miss = new MISSILE(5,shipActualX+shipWidth/2,shipActualY,0,4);
	return miss;
}

var fire2 = function (shipActualX,shipActualY,shipWidth,dir){
	var MISSILE = Java.type("pl.agit.Game.Sprites.Characters.BigMissile");
	var vvx=10;
	vvx = vvx*dir;
	var miss = new MISSILE(10,shipActualX+shipWidth/2,shipActualY,vvx,5);
	return miss;
}