# 🏆 Gloria Tournament — Introduction to Programming, Project II

<p>
  <img alt="Java" src="https://img.shields.io/badge/Java-1f2328?style=flat-square&logo=openjdk&logoColor=white">
  <img alt="Course" src="https://img.shields.io/badge/Course-Introduction%20to%20Programming%20(IP)-1f2328?style=flat-square">
  <img alt="Year" src="https://img.shields.io/badge/2022%2F23-1st%20year%2C%201st%20semester-1f2328?style=flat-square">
</p>

Second project of the course *Introdução à Programação* (Introduction to Programming), Computer Science degree at **NOVA School of Science and Technology (FCT NOVA)**. It builds on [Project I](https://github.com/MCCProgrammer/IP-Gloria-Game).

## About

A **knockout tournament** of the *Jogo da Glória* (the Portuguese *Game of the Goose*) for 3 to 10 players. Boards are loaded from a file, and a series of games is played on the chosen one: in each game a player is knocked out, until the winner of the last game takes the cup. Besides moving players around, the program keeps a live **ranking** (by games won, then by position on the board, then by turn order) and answers questions about whose turn it is, where a player is and whether they can roll the dice.

## What I practiced

- Splitting the problem into collaborating classes (`GloriaGame`, `Player`, `PlayerIterator`, `Main`)
- Implementing an **iterator** over the players still in the game
- Sorting with several tie-breaking criteria for the ranking
- Reading structured data from a text file and commands from standard input
- Code quality: preconditions, short methods and documented classes

## Running it

Requires Java 8 or newer. Run it from the project folder, where `boards.txt` is.

```bash
javac -d bin src/*.java
java -cp bin Main
```

The first line lists the players' colours (one letter each, in turn order) and the second chooses a board from `boards.txt`. Then send commands — `player`, `square <colour>`, `status <colour>`, `ranking`, `dice <d1> <d2>` and `exit`:

```text
RGBY
1
dice 2 4
dice 6 6
ranking
G: 0 games won; on square 13.
R: 0 games won; on square 7.
B: 0 games won; on square 1.
Y: 0 games won; on square 1.
exit
The cup was not over yet...
```

`boards.txt` has a sample board I wrote; the format is: number of squares, number of fine squares followed by `square penalty` lines, then number of cliffs followed by `square type` lines (`crab`, `hell` or `death`).

## Authors

- **Miguel Carmo** ([@MCCProgrammer](https://github.com/MCCProgrammer))
- **Tiago Pinto Pereira**

---

<sub>The assignment statement is not included in this repository, as it belongs to the course's teaching staff. This is a student project shared for portfolio purposes.</sub>
