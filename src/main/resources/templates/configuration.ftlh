<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Game Configuration</title>
  <link rel="stylesheet" href="/css/configuration.css">
</head>
<body>

<div id="config-container">
  <h2>Game Configuration</h2>

  <label for="symbol">Choose Symbol:</label>
  <select id="symbol">
    <option value="X">X</option>
    <option value="O">O</option>
  </select>

  <label for="board-format">Choose Board Format:</label>
  <select id="board-format">
    <option value="BOARD_3X3">3x3</option>
    <option value="BOARD_6X6">6x6</option>
    <option value="BOARD_9X9">9x9</option>
  </select>

  <label for="board-modification">Choose Board Modification:</label>
  <select id="board-modification">
    <option value="CLASSIC">Classic</option>
    <option value="CIRCLE">Circle</option>
    <option value="THOR">Thor</option>
  </select>

  <button onclick="startGame()">Start Game</button>
</div>

<script>
  function startGame() {
    const symbol = document.getElementById('symbol').value;
    const boardFormat = document.getElementById('board-format').value;
    const boardModification = document.getElementById('board-modification').value;

    window.location.href = 'game?' +
            'symbol=' + encodeURIComponent(symbol) +
            '&boardFormat=' + encodeURIComponent(boardFormat) +
            '&boardModification=' + encodeURIComponent(boardModification);
  }
</script>

</body>
</html>
