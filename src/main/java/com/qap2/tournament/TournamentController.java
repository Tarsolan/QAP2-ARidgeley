package com.qap2.tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/tournament")
public class TournamentController {
    private final TournamentService tournamentService;

    @Autowired
    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping
    public List<Tournament> getTournaments(){
        return tournamentService.getTournaments();
    }

    @GetMapping(path = "{tournamentID}")
    public Tournament getTournament(@PathVariable("tournamentID") Long tournamentID){
        return tournamentService.getTournament(tournamentID);
    }

    @PostMapping
    public void registerTournament(@RequestBody Tournament tournament){
        tournamentService.addTournament(tournament);
    }

    @DeleteMapping(path="{tournamentID}")
    public void deleteTournament(@PathVariable("tournamentID") Long tournamentID){
        tournamentService.deleteTournament(tournamentID);
    }

    @PutMapping(path="{tournamentID}")
    public void updateTournament(@PathVariable("tournamentID") Long tournamentID, @RequestBody Tournament tournament){
        tournamentService.updateTournament(tournamentID, tournament);
    }

    @PatchMapping(path="{tournamentID}")
    public void patchTournament(@PathVariable("tournamentID") Long tournamentID,
                                @RequestParam(required = false) LocalDate startDate,
                                @RequestParam(required = false) LocalDate endDate,
                                @RequestParam(required = false) String name,
                                @RequestParam(required = false) String location,
                                @RequestParam(required = false) Double entryFee,
                                @RequestParam(required = false) Double cashPrize
                                ) {
        tournamentService.patchTournament(tournamentID, startDate, endDate, name, location, entryFee, cashPrize);
    }

}
