/**
 * Creates a coordinate grid from p and q, like:
 *
 *   p-------------------+
 *   |                   |
 *   |                   |
 *   +-------------------q
 *   
 * @param p coord lat-lng coordinate
 * @param q coord lat-lng coordinate
 * @param partitions integer number of discrete blocks to make the grid ouf of
 */
function Grid(map, p, q, x_partitions, y_partitions) {
  this.map = map;

  this.grid = new Array(y_partitions);
  for (var i=0; i < this.grid.length; i++) {
    this.grid[i] = new Array(x_partitions);
  }

  if (p.lat > q.lat) {
    this.top    = p.lat;
    this.bottom = q.lat;
  } else {
    this.top    = q.lat;
    this.bottom = p.lat;
  }

  if (p.lng > q.lng) {
    this.left  = p.lng;
    this.right = q.lng;
  } else {
    this.left  = q.lng;
    this.right = p.lng;
  }

  this.x_partitions = x_partitions;
  this.y_partitions = y_partitions;
}

/**
 *
 */
Grid.prototype.nudge = function(latlng) {
  var pos = this.entry(latlng);
  var dx = this.right - this.left;
  var dy = this.top - this.bottom;
  return {
    lat: (dx+0.5) * pos.i,
    lng: (dy+0.5) * pos.j
  };
}

/**
 *
 */
Grid.prototype.entry = function(latlng) {
  var x = latlng.lat - this.left;
  var y = latlng.lng - this.bottom;

  var dx = this.right - this.left;
  var dy = this.top - this.bottom;

  return {
    i: Math.floor(x/dx),
    j: Math.floor(y/dy)
  };
}

/**
 *
 */
Grid.prototype.coord = function(i, j) {
  var dx = this.right - this.left;
  var dy = this.top - this.bottom;

  return {
    lat: dx*(0.5 + j) + this.left,
    lng: dy*(0.5 + i) + this.bottom
  };
}

/**
 *
 */
Grid.prototype.set = function(latlng, color) {
  var pos = this.entry(latlng);
  var nudged = this.nudge(latlng);
  this.grid[pos.i][pos.j] = new google.maps.Circle({center: latlng, radius: 200});
}

/**
 *
 */
Grid.prototype.get = function(latlng) {
}

/**
 *
 */
Grid.prototype.ping = function(latlng, color) {
  var entry = this.entry(latlng);
  console.log(entry);
  console.log(this.top, this.left);
  console.log(this.bottom, this.right);
}
