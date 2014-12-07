//nie radze formatowac widoku ctrl+f ;)

var byte2DArrayType = Java.type("byte[][]");
var byte1DArrayType = Java.type("byte[]");

	var map1 = new byte2DArrayType(3);
		map1[0] = Java.to([1,0,0,0,0,0,0,0,0,1],"byte[]"),
		map1[1] = Java.to([1,0,0,0,0,0,0,0,0,1],"byte[]"),
		map1[2] = Java.to([1,0,0,0,0,0,0,0,0,1],"byte[]");
	
	var map2 = new byte2DArrayType(3);
		map2[0] = Java.to([1,0,0,0,0,0,0,0,0,1],"byte[]"),
		map2[1] = Java.to([1,1,1,1,1,0,0,0,0,1],"byte[]"),
		map2[2] = Java.to([1,0,0,0,0,1,1,1,1,1],"byte[]");

	
	var map3 = new byte2DArrayType(3);
		map3[0] = Java.to([1,0,1,0,1,0,1,0,1,0],"byte[]"),
		map3[1] = Java.to([0,1,0,1,0,1,0,1,0,1],"byte[]"),
		map3[2] = Java.to([1,0,1,0,1,0,1,0,1,0],"byte[]");




var getMapList = function(){
	var ArrayList = Java.type("java.util.ArrayList")
	var lista = new ArrayList()
	
		lista.add(map1);
		//lista.add(map2);
		//lista.add(map3);
	return lista;
	
}