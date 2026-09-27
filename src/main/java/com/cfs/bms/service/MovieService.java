package com.cfs.bms.service;

import com.cfs.bms.dto.MovieDto;
import com.cfs.bms.exception.ResourceNotFoundException;
import com.cfs.bms.model.Movie;
import com.cfs.bms.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.client.ChatClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ChatClient.Builder chatClientBuilder;

    public MovieDto createMovie(MovieDto movieDto)
    {
        Movie movie=mapToEntity(movieDto);
        Movie saveMovie=movieRepository.save(movie);
        return mapToDto(saveMovie);
    }

    public MovieDto getMovieById(Long id)
    {
        Movie movie=movieRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Movie not found with id : "+id));
        return mapToDto(movie);
    }

    public List<MovieDto> getAllMovies()
    {
        List<Movie> movies=movieRepository.findAll();
        return movies.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<MovieDto> getMovieByLanguage(String language)
    {
        List<Movie> movies=movieRepository.findByLanguage(language);
        return movies.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<MovieDto> getMovieByGenre(String genre)
    {
        List<Movie> movies=movieRepository.findByGenre(genre);
        return movies.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    public List<MovieDto> searchMovies(String title)
    {
        List<Movie> movies=movieRepository.findByTitleContaining(title);
        return movies.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    public MovieDto updateMovie(Long id,MovieDto movieDto)
    {
        Movie movie=movieRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Movie not found with id : "+id));
        movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setLanguage(movieDto.getLanguage());
        movie.setGenre(movieDto.getGenre());
        movie.setDurationMins(movieDto.getDurationMins());
        movie.setReleaseDate(movieDto.getReleaseDate());
        movie.setPosterUrl(movieDto.getPosterUrl());

        Movie updatedMovie = movieRepository.save(movie);
        return mapToDto(updatedMovie);
    }

    public void deleteMovie(Long id)
    {
        Movie movie=movieRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Movie not found with id : "+id));
        movieRepository.delete(movie);
    }


    private MovieDto mapToDto(Movie movie)
    {
        MovieDto movieDto=new MovieDto();
        movieDto.setId(movie.getId());
        movieDto.setTitle(movie.getTitle());
        movieDto.setDescription(movie.getDescription());
        movieDto.setLanguage(movie.getLanguage());
        movieDto.setGenre(movie.getGenre());
        movieDto.setDurationMins(movie.getDurationMins());
        movieDto.setReleaseDate(movie.getReleaseDate());
        movieDto.setPosterUrl(movie.getPosterUrl());
        return movieDto;
    }

    public Movie mapToEntity(MovieDto movieDto)
    {
        Movie movie=new Movie();
        movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setLanguage(movieDto.getLanguage());
        movie.setGenre(movieDto.getGenre());
        movie.setDurationMins(movieDto.getDurationMins());
        movie.setReleaseDate(movieDto.getReleaseDate());
        movie.setPosterUrl(movieDto.getPosterUrl());
        return movie;
    }

    public String recommendMovies(String userPreference)
    {
        List<Movie> allMovies = movieRepository.findAll();

        StringBuilder movieList = new StringBuilder();
        for (Movie movie : allMovies) {
            movieList.append("- ").append(movie.getTitle())
                    .append(" | Genre: ").append(movie.getGenre())
                    .append(" | Language: ").append(movie.getLanguage())
                    .append(" | Description: ").append(movie.getDescription())
                    .append("\n");
        }

        String prompt = """
            You are a movie recommendation assistant for a ticket booking app.
            Here is the list of currently available movies:
            %s

            Based on the user's preference below, recommend the best 2-3 matching movies
            from the list above ONLY (do not suggest movies not in the list).
            Give a short 1-line reason for each recommendation.

            User preference: "%s"
            """.formatted(movieList.toString(), userPreference);

        ChatClient chatClient = chatClientBuilder.build();

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
