# valentine-miselm-solution
This is valentine challenge solution implemented in Android Studio.


### Table of Contents

* [Description]
* [Sample Usage]
  * [Parsing GeoJSON]
  * [Creating GeoJSON]

  
### Description

This solution generate GeoJson with heart, arrow and love you text. GeoJSON create heart at given position(latitude, longutude) and the size depends on given radiusInMeters.


### Sample Usage

 ##### Create geoJSON
For generating GeoJson with heart, arrow and love you text call method generateHeart with parameter latitude, longitude and radius in meters.

````java
FeatureCollection geoJson = GeneratePointsHelper.generateHeart(latitude, longitude, radiusInMeters);
JSONObject geoJSONObject = geoJson.toJSON();
````

 ##### Save GeoJSON
To save geoJson to file use WriteToFileHelper.saveToFile. GeoJson will be saved in document folder on external storage.

````java
WriteToFileHelper.saveToFile(this, geoJSON);
````