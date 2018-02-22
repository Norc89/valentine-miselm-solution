# valentine-miselm-solution
This is valentine challenge solution implemented in Android Studio.

### Table of Contents

* [Requirements](#requirements)
* [Installation](#installation)
* [Sample Usage](#sample-usage)
  * [Parsing GeoJSON](#parsing-geojson)
  * [Creating GeoJSON](#creating-geojson)


###Sample Usage


For generating GeoJson with heart, arrow and love you text call method generateHeart with parameter latitude, longitude and radius in meters.
 ##### String
````java
FeatureCollection geoJson = GeneratePointsHelper.generateHeart(latitude, longitude, radiusInMeters);
JSONObject geoJSONObject = geoJson.toJSON();
````

To save geoJson to file use WriteToFileHelper.saveToFile. GeoJson will be saved in document folder on external storage.
 ##### String
````java
WriteToFileHelper.saveToFile(this, geoJSON);
````