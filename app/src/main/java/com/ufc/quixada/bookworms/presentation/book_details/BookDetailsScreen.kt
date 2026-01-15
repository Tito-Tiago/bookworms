package com.ufc.quixada.bookworms.presentation.book_details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ufc.quixada.bookworms.R.drawable.ic_teclado
import com.ufc.quixada.bookworms.domain.model.ShelfType
import com.ufc.quixada.bookworms.presentation.components.BookwormsButton
import com.ufc.quixada.bookworms.presentation.components.RatingStars
import com.ufc.quixada.bookworms.presentation.components.ReviewCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(
    onNavigateBack: () -> Unit,
    viewModel: BookDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    var showShelfModal by remember { mutableStateOf(false) }
    var showReportModal by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val reportSheetState = rememberModalBottomSheetState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage ?: "Erro desconhecido",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                uiState.book?.let { book ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            contentAlignment = Alignment.TopCenter,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .background(MaterialTheme.colorScheme.primary)
                            )

                            Box(
                                modifier = Modifier
                                    .padding(top = 100.dp)
                            ) {
                                Card(
                                    elevation = CardDefaults.cardElevation(12.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier
                                        .width(180.dp)
                                        .height(270.dp)
                                ) {
                                    if (!book.capaUrl.isNullOrEmpty()) {
                                        AsyncImage(
                                            model = book.capaUrl,
                                            contentDescription = "Capa de ${book.titulo}",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color.Gray),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Book,
                                                contentDescription = null,
                                                modifier = Modifier.size(64.dp),
                                                tint = Color.White
                                            )
                                        }
                                    }
                                }

                                IconButton(
                                    onClick = { viewModel.onFavoriteClick() },
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .offset(x = 20.dp, y = 20.dp)
                                        .size(56.dp)
                                        .shadow(8.dp, CircleShape)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary)
                                ) {
                                    Icon(
                                        imageVector = if (uiState.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = if (uiState.isFavorite) "Desfavoritar" else "Favoritar",
                                        tint = if (uiState.isFavorite) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Text(
                            text = book.titulo,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.5.sp
                            ),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = book.autor,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(24.dp))
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Icon(Icons.Default.Star, null, tint = Color(0xFFFFD700), modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("%.1f".format(book.notaMediaComunidade), fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { showShelfModal = true },
                                shape = RoundedCornerShape(24.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (uiState.shelfType != null) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Icon(
                                    imageVector = if (uiState.shelfType != null) Icons.Default.Check else Icons.Default.LibraryAdd,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (uiState.shelfType != null) {
                                        when(uiState.shelfType) {
                                            ShelfType.LIDO -> "Lido"
                                            ShelfType.LENDO -> "Lendo"
                                            ShelfType.QUERO_LER -> "Quero Ler"
                                            else -> "Na Estante"
                                        }
                                    } else "Adicionar à Estante"
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                        ) {
                            if (book.sinopse.isNotEmpty()) {
                                Text(
                                    text = "Sobre o livro",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = book.sinopse,
                                    style = MaterialTheme.typography.bodyLarge,
                                    lineHeight = 28.sp,
                                    textAlign = TextAlign.Start,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                )
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 8.dp
                                ),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = "Toque para dar uma nota",
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    RatingStars(
                                        rating = uiState.nota,
                                        onRatingChanged = { newRating ->
                                            viewModel.onRatingChanged(newRating)
                                        },
                                        horizontalArrangement = Arrangement.Center
                                    )

                                    OutlinedTextField(
                                        value = uiState.textoResenha,
                                        onValueChange = { newText ->
                                            viewModel.onTextoResenhaChanged(newText)
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                        placeholder = {
                                            Text("Escreva sua resenha", color = Color.Gray)
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(128.dp)
                                            .padding(horizontal = 16.dp, vertical = 8.dp)
                                    )

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(4.dp)
                                    ) {
                                        Checkbox(
                                            checked = uiState.contemSpoiler,
                                            enabled = uiState.textoResenha.isNotBlank(),
                                            onCheckedChange = {
                                                viewModel.toggleContemSpoiler()
                                            }
                                        )
                                        Text(
                                            text = "Contém spoilers",
                                            fontSize = 16.sp
                                        )
                                    }

                                    BookwormsButton(
                                        text = "Fazer resenha",
                                        onClick = { viewModel.onFazerResenhaClick() },
                                        enabled = uiState.nota > 0,
                                        icon = painterResource(ic_teclado),
                                        iconContentDescription = "Icone de teclado",
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(32.dp))
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(32.dp))

                            if (uiState.reviews.isEmpty()) {
                                Text(text = "Nenhuma resenha até agora, seja o primeiro a comentar!", color = Color.Gray)
                            } else {
                                Column {
                                    uiState.reviews.forEach { review ->
                                        uiState.book?.titulo?.let {
                                            ReviewCard(
                                                userName = review.userName,
                                                bookName = it,
                                                nota = review.nota,
                                                textoResenha = review.textoResenha,
                                                onTreeDotsClick = { showReportModal = true },
                                                contemSpoiler = review.contemSpoiler
                                            )
                                            Spacer(modifier = Modifier.height(24.dp))
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(32.dp))
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }

    if (showShelfModal) {
        ModalBottomSheet(
            onDismissRequest = { showShelfModal = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    text = "Adicionar à estante",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
                )

                val options = listOf(
                    Triple(ShelfType.LIDO, "Lido", Icons.Default.Done),
                    Triple(ShelfType.LENDO, "Lendo", Icons.Default.AutoStories),
                    Triple(ShelfType.QUERO_LER, "Quero Ler", Icons.Default.Bookmark)
                )

                options.forEach { (type, label, icon) ->
                    val isSelected = uiState.shelfType == type
                    ListItem(
                        headlineContent = { Text(label) },
                        leadingContent = {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        trailingContent = if (isSelected) {
                            { Icon(Icons.Default.Check, null, tint = MaterialTheme.colorScheme.primary) }
                        } else null,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                            .clickable {
                                viewModel.onShelfSelected(type)
                                showShelfModal = false
                            },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                TextButton(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Adicionar uma nova estante")
                }
            }
        }
    }

    if (showReportModal) {
        ModalBottomSheet(
            onDismissRequest = { showReportModal = false },
            sheetState = reportSheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    text = "Denunciar Avaliação",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
                )

                val reportReasons = listOf(
                    "Conteúdo Ofensivo",
                    "Spam",
                    "Spoiler não marcado",
                    "Outro"
                )

                reportReasons.forEach { reason ->
                    ListItem(
                        headlineContent = { Text(reason) },
                        modifier = Modifier.clickable {
                            showReportModal = false
                        }
                    )
                }
            }
        }
    }
}