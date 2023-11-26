// Constants
const boardContainer = document.getElementById('board');

const urlParams = new URLSearchParams(window.location.search);

const boardModification = urlParams.get('boardModification');
const boardFormat = urlParams.get('boardFormat');
const boardSize = parseInt(boardFormat.charAt(boardFormat.length - 1));

const userSymbol = urlParams.get('symbol');
const botSymbol = userSymbol === "X" ? "O" : "X";

console.log(userSymbol)
console.log(botSymbol)

// Initialize the board matrix
const boardMatrix = Array.from({ length: boardSize }, () =>
    Array.from({ length: boardSize }, () => "EMPTY")
);


// Function to create the game board based on the specified format
function createBoard() {
    boardContainer.innerHTML = ''; // Clear existing board

    // Set a maximum width for the board
    const maxWidth = 400;

    // Calculate the width of each cell based on the specified format
    const cellWidth = Math.min(maxWidth / boardSize, 100);

    boardContainer.style.gridTemplateColumns = `repeat(${boardSize}, ${cellWidth}px)`;

    for (let i = 0; i < boardSize; i++) {
        for (let j = 0; j < boardSize; j++) {
            const cell = document.createElement('div');
            cell.classList.add('cell');
            cell.dataset.row = i;
            cell.dataset.col = j;
            cell.addEventListener('click', () => onCellClick(i, j));
            cell.style.width = `${cellWidth}px`;
            cell.style.height = `${cellWidth}px`;
            boardContainer.appendChild(cell);
        }
    }
}


// Function to handle user's move
function onCellClick(row, col) {
    if (boardMatrix[row][col] !== "EMPTY") return;

    updateCell(row, col, userSymbol);


    makeBotMove();
}


// Function to update a specific cell on the board
function updateCell(row, col, symbol) {
    boardMatrix[row][col] = userSymbol;
    const cell = document.querySelector(`.cell[data-row="${row}"][data-col="${col}"]`);
    cell.textContent = symbol;
}


// Function to update the visual representation of the board
function updateBoard() {
    const cells = document.querySelectorAll('.cell');
    cells.forEach((cell) => {
        const row = parseInt(cell.dataset.row);
        const col = parseInt(cell.dataset.col);
        cell.textContent = boardMatrix[row][col];
    });
}

// Function to reset the game
function resetGame() {
    // Reset the 2D array (matrix) to null values
    boardMatrix.forEach(row => row.fill("EMPTY"));
    updateBoard();
}

// Function to make a move for the bot
function makeBotMove() {
    const data = {
        "format": boardFormat,
        "modification": boardModification,
        "matrix": boardMatrix
    }

    console.log(boardMatrix);

    fetch("/game/optimal-move", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(move => {
            const row = move["cell"].row;
            const col = move["cell"].col;

            updateCell(row, col, botSymbol);
        })
}

// Initialize the board on page load
createBoard();

// Uncomment the line below to initiate the first move (user's move)
// makeBotMove();