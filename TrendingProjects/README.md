# Trending Projects
Hi, thank you for making the time to check my project! I hope you find some value in it!

Please notice that this project was built using Android Studio preview **Arctic Fox Beta 5**.

## Description
The app retrieves GitHub repositories from the [Search Repositories Api](https://docs.github.com/en/rest/reference/search#search-repositories) endpoint, sorting it by the number of stars (simulating the "trending" list).

- On the list, you can change the ordering (ascending or descending) on the filter menu.
- You can also filter the results selecting the period you want to search for (just as we have in the trending website).

If you select a repository, you will be taken to the detail screen, where you can:

- Check the number of stars, as well as the description.
- Navigate to the author's GitHub profile (click the avatar image).
- Navigate to the project page (menu icon).
- Check the languages being used in the project: these are retrieved when you open this screen, by hitting the [Repo Api](https://docs.github.com/en/rest/reference/repos#list-repository-languages).

**Implementation**
- Themes: Light and Dark, custom fonts (Work Sans), custom shapes
- Architecture pattern: MVVM + Clean (although I have used reactive states, we can easily build MVI on top of it if required)
- Jetpack: Paging3, Navigation, and many more
- DI: Koin
- Animations: shared element transitions with MaterialContainerTransform

**Testing Strategy** 
- Unit tests: ViewModels, UseCases, Paging Source
- Integration tests: Repos lists states (empty, error, success)

**Next steps**
- [ ] Improve testing coverage: extension  functions, utils, etc
- [ ] Extract core functionalities to a different module (lib:core)

One last thing, I also created a GitHub project [MVP](https://github.com/xapo-android-interviews/test-project-gusgol/projects/3). Although it does not reflect every thing that I did, it is very close to how I organize my work.


