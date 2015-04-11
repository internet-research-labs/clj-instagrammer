var viz = [
  {
    "featureType": "water",
    "stylers": [
      { "hue": "#0055ff" },
      { "visibility": "simplified" },
      { "lightness": 77 }
    ]
  },{
    "featureType": "road",
    "stylers": [
      { "visibility": "off" }
    ]
  },{
    "featureType": "landscape.natural",
    "stylers": [
      { "saturation": -41 },
      { "lightness": -19 }
    ]
  },{
    "featureType": "landscape.man_made",
    "stylers": [
      { "hue": "#ffdd00" },
      { "saturation": -91 },
      { "lightness": -62 }
    ]
  },{
    "featureType": "poi",
    "stylers": [
      { "visibility": "simplified" },
      { "saturation": -86 },
      { "lightness": -62 },
      { "hue": "#00ddff" }
    ]
  },{
    "elementType": "labels.text",
    "stylers": [
      { "visibility": "off" }
    ]
  },{
    "elementType": "labels",
    "stylers": [
      { "visibility": "off" }
    ]
  },{
    "featureType": "administrative",
    "stylers": [
      { "visibility": "off" }
    ]
  },{
  }
];

/**
 * Ping a location with a geometry shape
 * @param latlng {latlng-literal} latitutude/longitude literal {:lat ... :lng ...}
 * @param map    {GMap} google map object
 * @return undefined undefined
 */
function ping(latlng, map) {
  var circle = new google.maps.Circle({center: latlng, radius: 100});
  circle.setMap(map);
}

function pingCircle(latlng, map) {
}

function initialize() {
  var mapOptions = {
    // center: { lat: -34.397, lng: 150.644 },
    center: { lat: 40.7127, lng: -74.0059 },
    zoom: 13,
    styles: viz
  };
  
  var map = new google.maps.Map(document.getElementById('map'), mapOptions);
  ping({ lat: 40.7127, lng: -74.0059 }, map);
}

google.maps.event.addDomListener(window, 'load', initialize);
