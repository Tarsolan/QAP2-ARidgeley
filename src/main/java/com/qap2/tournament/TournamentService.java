package com.qap2.tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;

    @Autowired
    public TournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    public Tournament getTournament(Long tournamentID){
        return tournamentRepository.findById(tournamentID)
                .orElseThrow(() -> new IllegalStateException("Error! Tournament with ID " + tournamentID + " does not exist!"));
    }

    public List<Tournament> getTournaments(){
        return tournamentRepository.findAll();
    }

    public void addTournament(Tournament tournament){
        Optional<Tournament> tournamentOptional = tournamentRepository.findTournamentByName(tournament.getName());
        if (tournamentOptional.isPresent()) throw new IllegalStateException("Error! That name is already in use");
        else tournamentRepository.save(tournament);
    }

    public void deleteTournament(Long tournamentID) {
        boolean exists = tournamentRepository.existsById(tournamentID);
        if (!exists) throw new IllegalStateException("Error! Tournament with ID " + tournamentID + " does not exist!");
        else tournamentRepository.deleteById(tournamentID);
    }

    @Transactional
    public void patchTournament(Long tournamentID, LocalDate startDate, LocalDate endDate,
                                 String name, String location, Double entryFee, Double cashPrize){
        Tournament tournament = tournamentRepository.findById(tournamentID)
                .orElseThrow(() -> new IllegalStateException("Error! Tournament with ID " + tournamentID + " does not exist!"));

        if (startDate != null && !Objects.equals(tournament.getStartDate(), startDate)){
            if (startDate.isBefore(endDate != null ? endDate : tournament.getEndDate())) tournament.setStartDate(startDate);
            else throw new IllegalStateException("Error! Start Date must be before the End Date!");
        }

        if (endDate != null && !Objects.equals(tournament.getStartDate(), startDate)){
            if (endDate.isAfter(startDate != null ? startDate : tournament.getStartDate())) tournament.setStartDate(startDate);
            else throw new IllegalStateException("Error! End Date must be after the Start Date!");
        }

        if (name != null && name.length() > 0 && !Objects.equals(tournament.getName(), name)){
            Optional<Tournament> tournamentOptional = tournamentRepository.findTournamentByName(name);
            if (tournamentOptional.isPresent()) throw new IllegalStateException("Error! That name is taken.");
            tournament.setName(name);
        }

        if (location != null && location.length() > 0 && !Objects.equals(tournament.getLocation(), location)){
            tournament.setLocation(location);
        }

        if (entryFee != null && entryFee > 0.0 && !Objects.equals(tournament.getEntryFee(), entryFee)){
            tournament.setEntryFee(entryFee);
        }

        if (cashPrize != null && cashPrize > 0.0 && !Objects.equals(tournament.getCashPrize(), cashPrize)){
            tournament.setCashPrize(cashPrize);
        }
    }

    @Transactional
    public void updateTournament(Long tournamentID, Tournament tournament){
        Tournament tournamentToUpdate = tournamentRepository.findById(tournamentID)
                .orElseThrow(() -> new IllegalStateException("Error! Tournament with ID " + tournamentID + " does not exist!"));

        Optional<Tournament> tournamentOptional = tournamentRepository.findTournamentByName(tournament.getName());
        if (tournamentOptional.isPresent()) throw new IllegalStateException("Error! That name is taken.");
        tournamentToUpdate.setName(tournament.getName());

        if (!tournament.getStartDate().isBefore(tournament.getEndDate()))
            throw new IllegalStateException("Error! Start Date must be before the End Date!");
        tournamentToUpdate.setStartDate(tournament.getStartDate());

        if (!tournament.getEndDate().isAfter(tournament.getStartDate()))
            throw new IllegalStateException("Error! End Date must be after the Start Date!");
        tournamentToUpdate.setEndDate(tournament.getEndDate());

        tournamentToUpdate.setLocation(tournament.getLocation());
        tournamentToUpdate.setEntryFee(tournament.getEntryFee());
        tournamentToUpdate.setCashPrize(tournament.getCashPrize());
    }
}
