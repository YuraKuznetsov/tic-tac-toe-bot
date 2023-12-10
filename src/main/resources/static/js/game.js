// Constants
const boardContainer = document.getElementById('board');

const urlParams = new URLSearchParams(window.location.search);

const boardModification = urlParams.get('boardModification');
const boardFormat = urlParams.get('boardFormat');
const boardSize = parseInt(boardFormat.charAt(boardFormat.length - 1));

const userSymbol = urlParams.get('symbol');
const botSymbol = userSymbol === "X" ? "O" : "X";

// Initialize the board matrix
const boardMatrix = Array.from({ length: boardSize }, () =>
    Array.from({ length: boardSize }, () => "EMPTY")
);

let waitingResponse = false


// Function to create the game board based on the specified format
function createBoard() {
    boardContainer.innerHTML = '';

    const maxWidth = 400;
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

function checkGameStatus() {
    const data = {
        "format": boardFormat,
        "modification": boardModification,
        "matrix": boardMatrix
    }

    fetch("/game/status", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    })
        .then(response => response.text())
        .then(status => {
            console.log(status)
            if (status !== "Not finished") {
                alert(status);
            }
        })
}

// Function to handle user's move
function onCellClick(row, col) {
    if (waitingResponse) return;
    if (boardMatrix[row][col] !== "EMPTY") return;

    updateCell(row, col, userSymbol);
    // has user won
    checkGameStatus();
    makeBotMove();
    // has bot won
}


// Function to update a specific cell on the board
function updateCell(row, col, symbol) {
    boardMatrix[row][col] = symbol;
    const cell = document.querySelector(`.cell[data-row="${row}"][data-col="${col}"]`);
    cell.textContent = symbol;
}

function makeBotMove() {
    waitingResponse = true;
    const data = {
        "format": boardFormat,
        "modification": boardModification,
        "matrix": boardMatrix
    }

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
            checkGameStatus()
            waitingResponse = false;
        });
}

document.getElementById('user-symbol').textContent = userSymbol;
document.getElementById('board-modification').textContent = boardModification;

createBoard();

if (botSymbol === "X") {
    makeBotMove();
}