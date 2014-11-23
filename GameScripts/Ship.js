/**
 * Scripts for main space ship
 */

var reduceEnergy = function (energy, damage){
	return energy-2*damage;
};

var fire = function (shipActualX,shipActualY,shipWidth){
	var MISSILE = Java.type("pl.agit.Game.Sprites.Characters.Missile");
	var miss = new MISSILE(20,shipActualX+shipWidth/2,shipActualY,0,4);
	return miss;
}