# valentine-miselm-solution
This is valentine challenge solution implemented in Android Studio.


  
### Description

This solution generate GeoJson with heart, arrow and 'love you' text. GeoJSON create heart at given position(latitude, longutude) and the size depends on given radiusInMeters.

<table>
  <tr>
    <td align="center">
		<img width="50%" src="https://github.com/Norc89/valentine-miselm-solution/blob/master/Screenshots/image.jpg">
    </td>    
  </tr>
</table>

<a href="http://bl.ocks.org/d/04cb712c70b4e1d0889c25082518eed1">Example GeoJSON.io </a>

	 
### Sample Usage

 ##### Create geoJSON
For generating GeoJson with heart, arrow and 'love you' text call method generateHeart with parameter latitude, longitude and radius in meters.

````java
FeatureCollection geoJson = GeneratePointsHelper.generateHeart(latitude, longitude, radiusInMeters);
JSONObject geoJSONObject = geoJson.toJSON();
````

 ##### Save GeoJSON
To save geoJson to file use WriteToFileHelper.saveToFile. GeoJson.json will be saved in document folder on external storage in device.

````java
WriteToFileHelper.saveToFile(this, geoJSON);
````
