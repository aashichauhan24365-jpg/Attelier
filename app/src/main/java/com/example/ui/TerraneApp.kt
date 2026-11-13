package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesomeMosaic
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.outlined.AutoAwesomeMosaic
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.R
import com.example.data.CatalogRepository
import com.example.ui.screens.AtelierNotesSheet
import com.example.ui.screens.CatalogScreen
import com.example.ui.screens.LookbookScreen
import com.example.ui.screens.PaletteStudioScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.QuickViewSheet
import com.example.ui.screens.SavedScreen
import com.example.ui.screens.VignetteLookbookSheet
import com.example.ui.theme.TerraneThemeExtras
import com.example.viewmodel.TerraneViewModel

const val ROUTE_LOOKBOOK = "lookbook"
const val ROUTE_CATALOG = "catalog"
const val ROUTE_PALETTES = "palettes"
const val ROUTE_SAVED = "saved"
const val ROUTE_PRODUCT_DETAIL = "product_detail/{productId}"

private data class TopDestination(
    val route: String,
    val labelRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String,
)

private val topDestinations = listOf(
    TopDestination(
        route = ROUTE_LOOKBOOK,
        labelRes = R.string.nav_lookbook,
        selectedIcon = Icons.Filled.AutoAwesomeMosaic,
        unselectedIcon = Icons.Outlined.AutoAwesomeMosaic,
        testTag = "nav_lookbook",
    ),
    TopDestination(
        route = ROUTE_CATALOG,
        labelRes = R.string.nav_catalog,
        selectedIcon = Icons.Filled.GridView,
        unselectedIcon = Icons.Outlined.GridView,
        testTag = "nav_catalog",
    ),
    TopDestination(
        route = ROUTE_PALETTES,
        labelRes = R.string.nav_palettes,
        selectedIcon = Icons.Filled.Palette,
        unselectedIcon = Icons.Outlined.Palette,
        testTag = "nav_palettes",
    ),
    TopDestination(
        route = ROUTE_SAVED,
        labelRes = R.string.nav_spaces,
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder,
        testTag = "nav_saved",
    ),
)

@Composable
fun TerraneApp(
    viewModel: TerraneViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val isTopLevelRoute = topDestinations.any { dest ->
        currentDestination?.hierarchy?.any { it.route == dest.route } == true
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val colors = TerraneThemeExtras.colors

    LaunchedEffect(uiState.snackbarMessage) {
        val message = uiState.snackbarMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(
            message = message,
            duration = SnackbarDuration.Short,
        )
        viewModel.clearSnackbar()
    }

    fun navigateTopLevel(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun openProductDetail(productId: String) {
        navController.navigate("product_detail/$productId")
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.canvas),
    ) {
        val useNavigationRail = maxWidth >= 600.dp
        val productColumns = when {
            maxWidth >= 840.dp -> 3
            maxWidth >= 600.dp -> 3
            else -> 2
        }

        Scaffold(
            containerColor = colors.canvas,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    Snackbar(
                        containerColor = colors.inkPrimary,
                        contentColor = colors.canvas,
                        shape = CircleShape,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    ) {
                        Text(
                            text = data.visuals.message,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            },
            bottomBar = {
                if (isTopLevelRoute && !useNavigationRail) {
                    Box {
                        HorizontalDivider(color = colors.hairline)
                        NavigationBar(
                            containerColor = colors.canvas,
                            tonalElevation = 0.dp,
                        ) {
                            topDestinations.forEach { dest ->
                                val selected = currentDestination?.hierarchy?.any { it.route == dest.route } == true
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = { navigateTopLevel(dest.route) },
                                    icon = {
                                        NavIconWithOptionalBadge(
                                            dest = dest,
                                            selected = selected,
                                            moodboardCount = uiState.moodboardCount,
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = stringResource(dest.labelRes),
                                            style = MaterialTheme.typography.labelSmall,
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = colors.primaryAccent,
                                        selectedTextColor = colors.primaryAccent,
                                        indicatorColor = colors.alabasterFill,
                                        unselectedIconColor = colors.inkSecondary,
                                        unselectedTextColor = colors.inkSecondary,
                                    ),
                                    modifier = Modifier.testTag(dest.testTag),
                                )
                            }
                        }
                    }
                }
            },
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                if (isTopLevelRoute && useNavigationRail) {
                    NavigationRail(
                        containerColor = colors.canvas,
                        modifier = Modifier
                            .fillMaxHeight()
                            .windowInsetsPadding(WindowInsets.statusBars)
                            .padding(top = 12.dp),
                    ) {
                        Spacer(modifier = Modifier.weight(0.2f))
                        topDestinations.forEach { dest ->
                            val selected = currentDestination?.hierarchy?.any { it.route == dest.route } == true
                            NavigationRailItem(
                                selected = selected,
                                onClick = { navigateTopLevel(dest.route) },
                                icon = {
                                    NavIconWithOptionalBadge(
                                        dest = dest,
                                        selected = selected,
                                        moodboardCount = uiState.moodboardCount,
                                    )
                                },
                                label = {
                                    Text(
                                        text = stringResource(dest.labelRes),
                                        style = MaterialTheme.typography.labelSmall,
                                    )
                                },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = colors.primaryAccent,
                                    selectedTextColor = colors.primaryAccent,
                                    indicatorColor = colors.alabasterFill,
                                    unselectedIconColor = colors.inkSecondary,
                                    unselectedTextColor = colors.inkSecondary,
                                ),
                                modifier = Modifier.testTag(dest.testTag),
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    VerticalDivider(color = colors.hairline)
                }

                val topSafeModifier = if (isTopLevelRoute) {
                    Modifier.windowInsetsPadding(WindowInsets.statusBars)
                } else {
                    Modifier
                }

                NavHost(
                    navController = navController,
                    startDestination = ROUTE_LOOKBOOK,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .then(topSafeModifier),
                ) {
                    composable(ROUTE_LOOKBOOK) {
                        LookbookScreen(
                            uiState = uiState,
                            columns = productColumns,
                            onOpenProduct = ::openProductDetail,
                            onQuickView = viewModel::openQuickView,
                            onToggleSave = viewModel::toggleSaved,
                            onOpenVignette = viewModel::openVignetteSheet,
                            onOpenDecorIndex = { navigateTopLevel(ROUTE_CATALOG) },
                            onOpenPalettes = { navigateTopLevel(ROUTE_PALETTES) },
                            onOpenAtelierNotes = { viewModel.showAtelierNotes(true) },
                        )
                    }

                    composable(ROUTE_CATALOG) {
                        CatalogScreen(
                            uiState = uiState,
                            columns = productColumns,
                            onSearchQueryChange = viewModel::updateSearchQuery,
                            onSelectCategory = viewModel::selectCategory,
                            onSelectRoomFilter = viewModel::selectRoomFilter,
                            onToggleMaterialFilter = viewModel::toggleMaterialFilter,
                            onResetFilters = viewModel::resetFilters,
                            onOpenProduct = ::openProductDetail,
                            onQuickView = viewModel::openQuickView,
                            onToggleSave = viewModel::toggleSaved,
                        )
                    }

                    composable(ROUTE_PALETTES) {
                        PaletteStudioScreen(
                            uiState = uiState,
                            onSelectRoomFilter = viewModel::selectRoomFilter,
                            onToggleSavePalette = viewModel::toggleSavedPalette,
                            onOpenProduct = ::openProductDetail,
                        )
                    }

                    composable(ROUTE_SAVED) {
                        SavedScreen(
                            uiState = uiState,
                            columns = productColumns,
                            onOpenProduct = ::openProductDetail,
                            onQuickView = viewModel::openQuickView,
                            onToggleSave = viewModel::toggleSaved,
                            onToggleSavePalette = viewModel::toggleSavedPalette,
                            onExploreDecor = { navigateTopLevel(ROUTE_CATALOG) },
                            onExplorePalettes = { navigateTopLevel(ROUTE_PALETTES) },
                        )
                    }

                    composable(
                        route = ROUTE_PRODUCT_DETAIL,
                        arguments = listOf(navArgument("productId") { type = NavType.StringType }),
                    ) { entry ->
                        val productId = entry.arguments?.getString("productId") ?: ""
                        val product = CatalogRepository.findProduct(productId)
                            ?: CatalogRepository.products.first()
                        ProductDetailScreen(
                            product = product,
                            isSaved = product.id in uiState.savedProductIds,
                            dimensionUnit = uiState.dimensionUnit,
                            onToggleUnit = viewModel::toggleDimensionUnit,
                            onToggleSave = { viewModel.toggleSaved(product.id) },
                            onBack = { navController.popBackStack() },
                        )
                    }
                }
            }
        }
    }

    // Quick Inspect Bottom Sheet
    uiState.quickViewProduct?.let { product ->
        QuickViewSheet(
            product = product,
            isSaved = product.id in uiState.savedProductIds,
            dimensionUnit = uiState.dimensionUnit,
            onDismiss = viewModel::dismissQuickView,
            onToggleSave = { viewModel.toggleSaved(product.id) },
            onViewFullSpecifications = {
                viewModel.dismissQuickView()
                openProductDetail(product.id)
            },
        )
    }

    // Room Vignette Sheet
    uiState.activeVignetteSheet?.let { vignette ->
        VignetteLookbookSheet(
            vignette = vignette,
            allProducts = uiState.allProducts,
            onDismiss = viewModel::dismissVignetteSheet,
            onOpenProduct = { productId ->
                viewModel.dismissVignetteSheet()
                openProductDetail(productId)
            },
        )
    }

    // Atelier Notes Sheet
    if (uiState.atelierNotesVisible) {
        AtelierNotesSheet(
            onDismiss = { viewModel.showAtelierNotes(false) },
        )
    }
}

@Composable
private fun NavIconWithOptionalBadge(
    dest: TopDestination,
    selected: Boolean,
    moodboardCount: Int,
) {
    val colors = TerraneThemeExtras.colors
    val icon = if (selected) dest.selectedIcon else dest.unselectedIcon
    val label = stringResource(dest.labelRes)
    val badgeCount = if (dest.route == ROUTE_SAVED) moodboardCount else 0

    if (badgeCount > 0) {
        BadgedBox(
            badge = {
                Badge(
                    containerColor = colors.primaryAccent,
                    contentColor = colors.canvas,
                ) {
                    Text(text = "$badgeCount")
                }
            },
        ) {
            Icon(imageVector = icon, contentDescription = label)
        }
    } else {
        Icon(imageVector = icon, contentDescription = label)
    }
}
